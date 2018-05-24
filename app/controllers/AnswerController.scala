package controllers

import akka.actor.ActorSystem
import bmlogic.answer.answerMessage.{msg_checkAnswers, msg_pushAnswer, msg_randomAnswers, msg_randomGenerator}
import bmlogic.scroes.ScoresMessage.{msg_postAnswerScores, msg_preAnswerScores, msg_queryScores}
import bmlogic.user.UserMessage.{msg_pushUser, msg_queryUser}
import com.pharbers.bmmessages.{CommonModules, MessageRoutes}
import com.pharbers.bmpattern.LogMessage.msg_log
import com.pharbers.bmpattern.ResultMessage.msg_CommonResultMessage
import com.pharbers.dbManagerTrait.dbInstanceManager
import com.pharbers.token.AuthTokenTrait
import controllers.common.requestArgsQuery
import javax.inject.Inject
import play.api.mvc.Action
import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson

class AnswerController @Inject() (as_inject: ActorSystem, dbt : dbInstanceManager, att : AuthTokenTrait) {
    implicit val as: ActorSystem = as_inject
    lazy val raq = requestArgsQuery()
    import com.pharbers.bmpattern.LogMessage.common_log
    import com.pharbers.bmpattern.ResultMessage.common_result

    def pushAnswers = Action (request => raq.requestArgs(request) { jv =>
        MessageRoutes(msg_log(toJson(Map("method" -> toJson("push"))), jv)
            :: msg_pushAnswer(jv)
            :: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt, "att" -> att))))
    })

    def randomAnswers = Action (request => raq.requestArgs(request) { jv =>
        MessageRoutes(msg_log(toJson(Map("method" -> toJson("push"))), jv)
            :: msg_queryUser(jv)
            :: msg_pushUser(jv)
            :: msg_queryScores(jv)
//            :: msg_preAnswerScores(jv)
            :: msg_randomGenerator(jv)
            :: msg_randomAnswers(jv)
            :: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt, "att" -> att))))
    })

    def  checkAnswers = Action (request => raq.requestArgs(request) { jv =>
        MessageRoutes(msg_log(toJson(Map("method" -> toJson("push"))), jv)
            :: msg_queryUser(jv)
            :: msg_pushUser(jv)
            :: msg_checkAnswers(jv)
            :: msg_postAnswerScores(jv)
            :: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt, "att" -> att))))
    })
}
