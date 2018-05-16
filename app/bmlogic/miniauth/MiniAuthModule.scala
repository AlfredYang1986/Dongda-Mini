package bmlogic.miniauth

import bmlogic.miniauth.MiniAuthMessage.{msg_dropWXUnwanted, msg_queryWXAuthPara}
import com.pharbers.ErrorCode
import com.pharbers.bmmessages.{CommonModules, MessageDefines}
import com.pharbers.bmpattern.ModuleTrait
import com.pharbers.http.HTTP
import play.api.libs.json.{JsObject, JsValue}
import play.api.libs.json.Json.toJson

object MiniAuthModule extends ModuleTrait {

    def dispatchMsg(msg: MessageDefines)(pr: Option[Map[String, JsValue]])(implicit cm: CommonModules): (Option[Map[String, JsValue]], Option[JsValue]) = msg match {
        case msg_queryWXAuthPara(data) => queryWXAuthPara(data)
        case msg_dropWXUnwanted(data) => dropWXUnwanted(data)(pr)
        case _ => ???
    }

    val appid = "wx6b85b33678a1dad6"
    val secret = "0597961696fcbfdd07368bbf631c2054"

    def queryWXAuthPara(data : JsValue)
                       (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {

        try {
            (data \ "wx" \ "code").asOpt[String].map { code =>
                val wx_result = HTTP(s"https://api.weixin.qq.com/sns/jscode2session?appid=$appid&secret=$secret&js_code=$code&grant_type=authorization_code").get
                val open_id = (wx_result \ "openid").asOpt[String].get
                val condition = (data \ "condition").asOpt[JsValue].map (x => x).getOrElse(toJson(Map("wx" -> toJson("code"))))
                val tmp = toJson(condition.as[JsObject].value.toMap ++ Map("wechat_id" -> toJson(open_id)))
                (Some(Map("condition" -> tmp, "open_id" -> toJson(open_id))), None)

            }.getOrElse((Some(Map("wx" -> toJson("no need"))), None))

        } catch {
            case ex : Exception => println(s"query wx auth.error=${ex.getMessage}");(None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }

    def dropWXUnwanted(data : JsValue)
                      (pr : Option[Map[String, JsValue]])
                      (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {
        try {
            val m = pr.map (x => x).getOrElse(Map.empty)

            val tmp = m - "condition"
            (Some(tmp), None)

        } catch {
            case ex : Exception => println(s"query wx auth.error=${ex.getMessage}");(None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }

}
