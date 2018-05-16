package controllers

import akka.actor.ActorSystem
import bmlogic.checkin.CheckInMessage.{msg_dropCheckedUnwanted, msg_isChecked, msg_pushCheckIn, msg_userCheckedLst}
import bmlogic.miniauth.MiniAuthMessage.{msg_dropWXUnwanted, msg_queryWXAuthPara}
import bmlogic.providers.ProvidersMessage.{msg_dropUnwantedMessage, msg_mergeCheckedProviderOne, msg_queryProviderOne}
import bmlogic.providerslevel.ProvidersLevelMessage.{msg_pushProvidersLevel, msg_queryProvidersLevel}
import bmlogic.scroes.ScoresMessage.{msg_addScores, msg_queryScores}
import bmlogic.user.UserMessage.{msg_pushUser, msg_queryUser}
import com.pharbers.bmmessages.{CommonModules, MessageRoutes}
import com.pharbers.bmpattern.LogMessage.msg_log
import com.pharbers.bmpattern.ResultMessage.msg_CommonResultMessage
import com.pharbers.dbManagerTrait.dbInstanceManager
import com.pharbers.token.AuthTokenTrait
import controllers.common.requestArgsQuery
import javax.inject.Inject
import play.api.libs.json.Json.toJson
import play.api.mvc.Action

class CheckInController @Inject() (as_inject: ActorSystem, dbt : dbInstanceManager, att : AuthTokenTrait) {
    implicit val as: ActorSystem = as_inject
    lazy val raq = requestArgsQuery()
    import com.pharbers.bmpattern.LogMessage.common_log
    import com.pharbers.bmpattern.ResultMessage.common_result

    def checkin = Action (request => raq.requestArgs(request) { jv =>
        MessageRoutes(msg_log(toJson(Map("method" -> toJson("push"))), jv)
            :: msg_queryUser(jv)
            :: msg_pushUser(jv)
            :: msg_queryProviderOne(jv)
            :: msg_isChecked(jv)
            :: msg_pushCheckIn(jv)
            :: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt, "att" -> att))))
    })

    def checkinScores = Action (request => raq.requestArgs(request) { jv =>
        MessageRoutes(msg_log(toJson(Map("method" -> toJson("push"))), jv)
            :: msg_queryWXAuthPara(jv)
            :: msg_queryUser(jv)
            :: msg_pushUser(jv)
            :: msg_queryProviderOne(jv)
            :: msg_userCheckedLst(jv)   //
            :: msg_mergeCheckedProviderOne(jv)  //
            :: msg_isChecked(jv)
            :: msg_pushCheckIn(jv)
            :: msg_queryProvidersLevel(jv)
            :: msg_queryScores(jv)
            :: msg_addScores(jv)
            :: msg_dropCheckedUnwanted(jv)
            :: msg_dropWXUnwanted(jv)
            :: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt, "att" -> att))))
    })

    def queryScores = Action (request => raq.requestArgs(request) { jv =>
        MessageRoutes(msg_log(toJson(Map("method" -> toJson("push"))), jv)
            :: msg_queryUser(jv)
            :: msg_pushUser(jv)
            :: msg_queryScores(jv)
            :: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt, "att" -> att))))
    })

    def pushLevel = Action (request => raq.requestArgs(request) { jv =>
        MessageRoutes(msg_log(toJson(Map("method" -> toJson("push"))), jv)
            :: msg_pushProvidersLevel(jv)
            :: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt, "att" -> att))))
    })

    def queryLevel = Action (request => raq.requestArgs(request) { jv =>
        MessageRoutes(msg_log(toJson(Map("method" -> toJson("push"))), jv)
            :: msg_queryProvidersLevel(jv)
            :: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt, "att" -> att))))
    })
}
