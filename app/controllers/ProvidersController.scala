package controllers

import akka.actor.ActorSystem
import bmlogic.checkin.CheckInMessage.msg_userCheckedLst
import bmlogic.providers.ProvidersMessage._
import bmlogic.providerslevel.ProvidersLevelMessage.{msg_mergeDisplayAges, msg_queryCollectedProviders, msg_queryDisplayAges, msg_queryTopProviders}
import bmlogic.user.UserMessage.{msg_pushUser, msg_queryUser}
import com.pharbers.bmmessages.{CommonModules, MessageRoutes}
import com.pharbers.bmpattern.LogMessage.msg_log
import com.pharbers.bmpattern.ResultMessage.msg_CommonResultMessage
import com.pharbers.dbManagerTrait.dbInstanceManager
import com.pharbers.token.AuthTokenTrait
import controllers.common.requestArgsQuery
import javax.inject.{Inject, Singleton}
import play.api.mvc.Action
import play.api.libs.json.Json.toJson

@Singleton
class ProvidersController @Inject() (as_inject: ActorSystem, dbt : dbInstanceManager, att : AuthTokenTrait) {
    implicit val as: ActorSystem = as_inject
    lazy val raq = requestArgsQuery()
    import com.pharbers.bmpattern.LogMessage.common_log
    import com.pharbers.bmpattern.ResultMessage.common_result

    def pushProvider = Action (request => raq.requestArgs(request) { jv =>
        MessageRoutes(msg_log(toJson(Map("method" -> toJson("push"))), jv)
            :: msg_pushProvider(jv)
            :: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt, "att" -> att))))
    })

    def popProvider = Action (request => raq.requestArgs(request) { jv =>
        MessageRoutes(msg_log(toJson(Map("method" -> toJson("pop"))), jv)
            :: msg_popProvider(jv)
            :: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt, "att" -> att))))
    })

    def queryProvider = Action (request => raq.requestArgs(request) { jv =>
        MessageRoutes(msg_log(toJson(Map("method" -> toJson("query"))), jv)
            :: msg_queryUser(jv)
            :: msg_pushUser(jv)
            :: msg_queryProvider(jv)
            :: msg_userCheckedLst(jv)
            :: msg_mergeCheckedProviderOne(jv)
            :: msg_queryTopProviders(jv)
            :: msg_mergeTopProviderOne(jv)
            :: msg_dropUnwantedMessage(jv)
            :: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt, "att" -> att))))
    })

    def searchProviders = Action (request => raq.requestArgs(request) { jv =>
        MessageRoutes(msg_log(toJson(Map("method" -> toJson("query"))), jv)
            :: msg_queryUser(jv)
            :: msg_pushUser(jv)
            :: msg_searchProvider(jv)
            :: msg_userCheckedLst(jv)
            :: msg_mergeCheckedProvider(jv)
            :: msg_queryTopProviders(jv)
            :: msg_mergeTopProvider(jv)
            :: msg_dropUnwantedMessage(jv)
            :: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt, "att" -> att))))
    })

    def collectedProviders = Action (request => raq.requestArgs(request) { jv =>
        MessageRoutes(msg_log(toJson(Map("method" -> toJson("collected"))), jv)
            :: msg_queryUser(jv)
            :: msg_pushUser(jv)
            :: msg_queryCollectedProviders(jv)
            :: msg_queryProviderMulti(jv)
            :: msg_userCheckedLst(jv)
            :: msg_mergeCheckedProvider(jv)
            :: msg_queryTopProviders(jv)
            :: msg_mergeTopProvider(jv)
            :: msg_queryDisplayAges(jv)
            :: msg_mergeDisplayAges(jv)
            :: msg_dropUnwantedMessage(jv)
            :: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt, "att" -> att))))
    })
}
