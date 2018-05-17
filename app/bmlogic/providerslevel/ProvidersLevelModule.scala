package bmlogic.providerslevel

import bmlogic.common.mergestepresult.MergeStepResult
import bmlogic.providerslevel.ProvidersLevelData._
import bmlogic.providerslevel.ProvidersLevelMessage._
import com.mongodb.casbah.Imports._
import com.pharbers.ErrorCode
import com.pharbers.bmmessages.{CommonModules, MessageDefines}
import com.pharbers.bmpattern.ModuleTrait
import com.pharbers.dbManagerTrait.dbInstanceManager
import play.api.libs.json.{JsObject, JsValue}
import play.api.libs.json.Json.toJson

object ProvidersLevelModule extends ModuleTrait {

    def dispatchMsg(msg: MessageDefines)(pr: Option[Map[String, JsValue]])(implicit cm: CommonModules): (Option[Map[String, JsValue]], Option[JsValue]) = msg match {
        case msg_pushProvidersLevel(data) => pushProviderLevel(data)
        case msg_queryProvidersLevel(data) => queryProviderLevel(data)(pr)
        case msg_queryCollectedProviders(data) => queryCollectedProviders(data)(pr)
        case msg_queryTopProviders(data) => queryTopProviders(data)(pr)
        case _ => ???
    }

    object inner_traits extends creation with condition with result

    def pushProviderLevel(data : JsValue)
                         (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {

        try {
            val conn = cm.modules.get.get("db").map(x => x.asInstanceOf[dbInstanceManager]).getOrElse(throw new Exception("no db connection"))
            val db = conn.queryDBInstance("cli").get

            import inner_traits.m2d
            val o : DBObject = data
            db.insertObject(o, "levels", "_id")
            val reVal = o.get("_id").asInstanceOf[ObjectId].toString

            (Some(Map("level_id" -> toJson(reVal))), None)
        } catch {
            case ex : Exception => println(s"push level.error=${ex.getMessage}");(None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }

    def queryProviderLevel(data : JsValue)
                          (pr : Option[Map[String, JsValue]])
                          (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {

        try {
            val conn = cm.modules.get.get("db").map(x => x.asInstanceOf[dbInstanceManager]).getOrElse(throw new Exception("no db connection"))
            val db = conn.queryDBInstance("cli").get

            val js = MergeStepResult(data, pr)
            val m = pr.map (x => x).getOrElse(Map.empty)

            import inner_traits.sc
            import inner_traits.sr
            val o : DBObject = js
            val reVal = db.queryObject(o, "levels")
                            .map (x => x.get("ll").get.asOpt[String].get).getOrElse("scores_A")

            (Some(m ++ Map("level" -> toJson(reVal))), None)

        } catch {
            case ex : Exception => println(s"query level.error=${ex.getMessage}");(None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }

    def queryCollectedProviders(data : JsValue)
                               (pr : Option[Map[String, JsValue]])
                               (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {

        try {
            val conn = cm.modules.get.get("db").map(x => x.asInstanceOf[dbInstanceManager]).getOrElse(throw new Exception("no db connection"))
            val db = conn.queryDBInstance("cli").get

            val js = MergeStepResult(data, pr)
            val m = pr.map (x => x).getOrElse(Map.empty)
            val condition = (js \ "condition").asOpt[JsValue].map (x => x).getOrElse(toJson(Map("wx" -> toJson("code"))))

            import inner_traits.spc
            import inner_traits.colr
            val o : DBObject = js
            val reVal = db.queryMultipleObject(o, "levels")
                            .map (x => x.get("provider_id").get.asOpt[String].get)

            val tmp = toJson(condition.as[JsObject].value.toMap ++ Map("providers" -> toJson(reVal)))
            (Some(m ++ Map("condition" -> tmp)), None)

        } catch {
            case ex : Exception => println(s"query collect.error=${ex.getMessage}");(None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }

    def queryTopProviders(data : JsValue)
                               (pr : Option[Map[String, JsValue]])
                               (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {

        try {
            val conn = cm.modules.get.get("db").map(x => x.asInstanceOf[dbInstanceManager]).getOrElse(throw new Exception("no db connection"))
            val db = conn.queryDBInstance("cli").get

            val js = MergeStepResult(data, pr)
            val m = pr.map (x => x).getOrElse(Map.empty)
            val condition = (js \ "condition").asOpt[JsValue].map (x => x).getOrElse(toJson(Map("wx" -> toJson("code"))))

            import inner_traits.tpc
            import inner_traits.colr
            val o : DBObject = js
            val reVal = db.queryMultipleObject(o, "levels")
                .map (x => x.get("provider_id").get.asOpt[String].get)

            val tmp = toJson(condition.as[JsObject].value.toMap ++ Map("providers" -> toJson(reVal)))
            (Some(m ++ Map("condition" -> tmp)), None)

        } catch {
            case ex : Exception => println(s"query top.error=${ex.getMessage}");(None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }
}
