package bmlogic.provider

import bmlogic.provider.ProviderData.{condition, creation, result}
import bmlogic.provider.ProviderMessage._
import com.mongodb.casbah.Imports._
import com.pharbers.ErrorCode
import com.pharbers.bmmessages.{CommonModules, MessageDefines}
import com.pharbers.bmpattern.ModuleTrait
import com.pharbers.dbManagerTrait.dbInstanceManager
import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson

object ProviderModule extends ModuleTrait {

    def dispatchMsg(msg: MessageDefines)(pr: Option[Map[String, JsValue]])(implicit cm: CommonModules): (Option[Map[String, JsValue]], Option[JsValue]) = msg match {
        case msg_pushProviderApply(data) => pushProviderApply(data)
        case msg_popProviderApply(data) => popProviderApply(data)
        case msg_queryProviderApply(data) => queryProviderApply(data)
        case msg_searchProviderApply(data) => searchProviderApply(data)
        case _ => ???
    }

    object inner_traits extends creation with result with condition

    def pushProviderApply(data : JsValue)
                         (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {
        try {
            val conn = cm.modules.get.get("db").map(x => x.asInstanceOf[dbInstanceManager]).getOrElse(throw new Exception("no db connection"))
            val db = conn.queryDBInstance("cli").get

            import inner_traits.m2d
            val o : DBObject = data
            db.insertObject(o, "provider_applies", "_id")
            val reVal = o.get("_id").asInstanceOf[ObjectId].toString

            (Some(Map("apply_id" -> toJson(reVal))), None)

        } catch {
            case ex : Exception => println(s"push.error=${ex.getMessage}");(None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }

    def popProviderApply(data : JsValue)
                (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {

        try {

            val conn = cm.modules.get.get("db").map(x => x.asInstanceOf[dbInstanceManager]).getOrElse(throw new Exception("no db connection"))
            val db = conn.queryDBInstance("cli").get

            import inner_traits.qc
            val o : DBObject = data
            db.deleteObject(o, "provider_applies", "_id")

            (Some(Map("pop apply" -> toJson("success"))), None)

        } catch {
            case ex : Exception => println(s"pop.error=${ex.getMessage}");(None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }

    def queryProviderApply(data : JsValue)
                          (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {
        try {

            val conn = cm.modules.get.get("db").map(x => x.asInstanceOf[dbInstanceManager]).getOrElse(throw new Exception("no db connection"))
            val db = conn.queryDBInstance("cli").get

            import inner_traits.qc
            import inner_traits.d2m
            val o : DBObject = data
            val reVal = db.queryObject(o, "provider_applies").get

            (Some(Map("apply" -> toJson(reVal))), None)

        } catch {
            case ex : Exception => println(s"pop.error=${ex.getMessage}");(None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }

    def searchProviderApply(data : JsValue)
                           (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {
        try {

            val conn = cm.modules.get.get("db").map(x => x.asInstanceOf[dbInstanceManager]).getOrElse(throw new Exception("no db connection"))
            val db = conn.queryDBInstance("cli").get

            val take = (data \ "take").asOpt[Int].map (x => x).getOrElse(20)
            val skip = (data \ "skip").asOpt[Int].map (x => x).getOrElse(0)

            import inner_traits.sc
            import inner_traits.d2m
            val o : DBObject = data
            val reVal = db.queryMultipleObject(o, "provider_applies", take = take, skip = skip)

            (Some(Map("applies" -> toJson(reVal))), None)

        } catch {
            case ex : Exception => println(s"pop.error=${ex.getMessage}");(None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }
}
