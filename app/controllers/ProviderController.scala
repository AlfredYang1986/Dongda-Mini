package controllers

import akka.actor.ActorSystem
import bmlogic.provider.ProviderMessage._
import com.pharbers.bmmessages.{CommonModules, MessageRoutes}
import com.pharbers.bmpattern.LogMessage.msg_log
import com.pharbers.bmpattern.ResultMessage.msg_CommonResultMessage
import com.pharbers.dbManagerTrait.dbInstanceManager
import com.pharbers.token.AuthTokenTrait
import controllers.common.requestArgsQuery
import javax.inject.{Inject, Singleton}
import play.api.mvc._
import play.api.libs.json.Json.toJson

@Singleton
class ProviderController @Inject() (as_inject: ActorSystem, dbt : dbInstanceManager, att : AuthTokenTrait) {
    implicit val as: ActorSystem = as_inject
    lazy val raq = requestArgsQuery()
    import com.pharbers.bmpattern.LogMessage.common_log
    import com.pharbers.bmpattern.ResultMessage.common_result

    def pushApplies = Action (request => raq.requestArgs(request) { jv =>
        MessageRoutes(msg_log(toJson(Map("method" -> toJson("push"))), jv)
            :: msg_pushProviderApply(jv)
            :: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt, "att" -> att))))
    })

    def popApplies = Action (request => raq.requestArgs(request) { jv =>
        MessageRoutes(msg_log(toJson(Map("method" -> toJson("pop"))), jv)
            :: msg_popProviderApply(jv)
            :: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt, "att" -> att))))
    })

    def queryApplies = Action (request => raq.requestArgs(request) { jv =>
        MessageRoutes(msg_log(toJson(Map("method" -> toJson("query"))), jv)
            :: msg_queryProviderApply(jv)
            :: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt, "att" -> att))))
    })

    def searchApplies = Action (request => raq.requestArgs(request) { jv =>
        MessageRoutes(msg_log(toJson(Map("method" -> toJson("query"))), jv)
            :: msg_searchProviderApply(jv)
            :: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt, "att" -> att))))
    })
}
