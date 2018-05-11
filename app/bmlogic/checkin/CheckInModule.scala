package bmlogic.checkin

import bmlogic.checkin.CheckInData._
import bmlogic.checkin.CheckInMessage._
import bmlogic.common.mergestepresult.MergeStepResult
import com.mongodb.casbah.Imports._
import com.pharbers.ErrorCode
import com.pharbers.bmmessages.{CommonModules, MessageDefines}
import com.pharbers.bmpattern.ModuleTrait
import com.pharbers.dbManagerTrait.dbInstanceManager
import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson


object CheckInModule extends ModuleTrait {

    def dispatchMsg(msg: MessageDefines)(pr: Option[Map[String, JsValue]])(implicit cm: CommonModules): (Option[Map[String, JsValue]], Option[JsValue]) = msg match {
        case msg_pushCheckIn(data) => pushCheckIn(data)(pr)
        case msg_isChecked(data) => isChecked(data)(pr)
        case _ => ???
    }

    object inner_traits extends creation with condition with result

    def pushCheckIn(data : JsValue)
                   (pr : Option[Map[String, JsValue]])
                   (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {
        try {
            val conn = cm.modules.get.get("db").map(x => x.asInstanceOf[dbInstanceManager]).getOrElse(throw new Exception("no db connection"))
            val db = conn.queryDBInstance("cli").get

            val js = MergeStepResult(data, pr)
            if ((js \ "is_check").asOpt[Int].map (x => x == 0).getOrElse(false)) {
                import inner_traits.m2d
                val o : DBObject = js
                db.insertObject(o, "checkin", "_id")
                val reVal = o.get("_id").asInstanceOf[ObjectId].toString

                (Some(Map("check_in" -> toJson(reVal))), None)
            } else {
                (Some(Map("check_in" -> toJson("already checked"))), None)
            }


        } catch {
            case ex : Exception => println(s"push.error=${ex.getMessage}");(None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }

    def isChecked(data : JsValue)
                   (pr : Option[Map[String, JsValue]])
                   (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {
        try {
            val conn = cm.modules.get.get("db").map(x => x.asInstanceOf[dbInstanceManager]).getOrElse(throw new Exception("no db connection"))
            val db = conn.queryDBInstance("cli").get

            val js = MergeStepResult(data, pr)
            val m = pr.map (x => x).getOrElse(Map.empty)

            import inner_traits.sc
            import inner_traits.d2m
            val o : DBObject = js
            db.queryObject(o, "checkin").map { _ =>
                (Some(Map("is_check" -> toJson(1)) ++ m), None)
            }.getOrElse(
                (Some(Map("is_check" -> toJson(0)) ++ m), None)
            )

        } catch {
            case ex : Exception => println(s"push.error=${ex.getMessage}");(None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }
}