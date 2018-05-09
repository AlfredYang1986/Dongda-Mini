package controllers

import akka.actor.ActorSystem
import bmlogic.provider.ProviderMessage.{msg_queryProviderApply, msg_searchProviderApply}
import com.pharbers.bmmessages.{CommonModules, MessageRoutes}
import com.pharbers.bmpattern.LogMessage.msg_log
import com.pharbers.bmpattern.ResultMessage.msg_CommonResultMessage
import com.pharbers.dbManagerTrait.dbInstanceManager
import com.pharbers.token.AuthTokenTrait
import controllers.common.requestArgsQuery
import javax.inject.{Inject, Singleton}
import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson
import play.api.mvc._

@Singleton
class AdminController @Inject() (as_inject : ActorSystem, dbt : dbInstanceManager, att : AuthTokenTrait) extends Controller {
    implicit val as: ActorSystem = as_inject
    lazy val raq = requestArgsQuery()
    import com.pharbers.bmpattern.LogMessage.common_log
    import com.pharbers.bmpattern.ResultMessage.common_result

    def queryProviderAppliesPage = Action {

        val jv = toJson(Map(
            "condition" -> toJson(Map(
                "approved" -> toJson(0)
            ))
        ))
        val tmp = raq.commonExcution(
            MessageRoutes(msg_log(toJson(Map("method" -> toJson("query"))), jv)
                :: msg_searchProviderApply(jv)
                :: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt, "att" -> att))))
        )

        val applies = (tmp \ "result" \ "applies").asOpt[List[JsValue]].get
        println(applies)

        Ok(views.html.providerapplies(applies))
    }

    def queryProviderApplyDetailPage(apply_id : String) = Action {

        println(apply_id)
        val jv = toJson(Map(
            "condition" -> toJson(Map(
                "apply_id" -> toJson(apply_id)
            ))
        ))
        val tmp = raq.commonExcution(
            MessageRoutes(msg_log(toJson(Map("method" -> toJson("query"))), jv)
                :: msg_queryProviderApply(jv)
                :: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt, "att" -> att))))
        )

        if ((tmp \ "status").asOpt[String].get == "error")
            Ok(views.html.providerapplyqueryerror("申请不存在"))
        else {
            val apply = (tmp \ "result" \ "apply").asOpt[JsValue].get
            println(apply)

            Ok(views.html.providerapplydetail(apply))
        }
    }
}
