package controllers

import akka.actor.ActorSystem
import bmlogic.answer.answerMessage.msg_resetRandomIndex
import bmlogic.providers.ProvidersMessage.{msg_exportProviders, msg_resetLocationPin, msg_resetProviderLogo, msg_resetProviderSearchId}
import bmlogic.providerslevel.ProvidersLevelMessage.msg_resetLevels
import com.pharbers.bmmessages.{CommonModules, MessageRoutes}
import com.pharbers.bmpattern.LogMessage.msg_log
import com.pharbers.bmpattern.ResultMessage.msg_CommonResultMessage
import com.pharbers.dbManagerTrait.dbInstanceManager
import com.pharbers.token.AuthTokenTrait
import controllers.common.requestArgsQuery
import javax.inject.Inject
import play.api.libs.json.Json.toJson
import play.api.mvc.Action

class CleanController @Inject() (as_inject: ActorSystem, dbt : dbInstanceManager, att : AuthTokenTrait) {
    implicit val as: ActorSystem = as_inject
    lazy val raq = requestArgsQuery()
    import com.pharbers.bmpattern.LogMessage.common_log
    import com.pharbers.bmpattern.ResultMessage.common_result

    def resetAnswers = Action (request => raq.requestArgs(request) { jv =>
        MessageRoutes(msg_log(toJson(Map("method" -> toJson("push"))), jv)
            :: msg_resetRandomIndex(jv)
            :: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt, "att" -> att))))
    })

    def resetLevels = Action (request => raq.requestArgs(request) { jv =>
        MessageRoutes(msg_log(toJson(Map("method" -> toJson("push"))), jv)
            :: msg_resetLevels(jv)
            :: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt, "att" -> att))))
    })

    def resetLogo = Action (request => raq.requestArgs(request) { jv =>
        MessageRoutes(msg_log(toJson(Map("method" -> toJson("push"))), jv)
            :: msg_resetProviderLogo(jv)
            :: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt, "att" -> att))))
    })

    def resetPin = Action (request => raq.requestArgs(request) { jv =>
        MessageRoutes(msg_log(toJson(Map("method" -> toJson("push"))), jv)
            :: msg_resetLocationPin(jv)
            :: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt, "att" -> att))))
    })

    def resetProvidersSearchId = Action (request => raq.requestArgs(request) { jv =>
        MessageRoutes(msg_log(toJson(Map("method" -> toJson("push"))), jv)
            :: msg_resetProviderSearchId(jv)
            :: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt, "att" -> att))))
    })

    def exportProviders = Action (request => raq.requestArgs(request) { jv =>
        MessageRoutes(msg_log(toJson(Map("method" -> toJson("push"))), jv)
            :: msg_exportProviders(jv)
            :: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt, "att" -> att))))
    })

}
