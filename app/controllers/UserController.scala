package controllers

import akka.actor.ActorSystem
import bmlogic.miniauth.MiniAuthMessage.{msg_dropWXUnwanted, msg_queryWXAuthPara}
import bmlogic.user.UserMessage._
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
class UserController @Inject() (as_inject: ActorSystem, dbt : dbInstanceManager, att : AuthTokenTrait) {
    implicit val as: ActorSystem = as_inject
    lazy val raq = requestArgsQuery()

    import com.pharbers.bmpattern.LogMessage.common_log
    import com.pharbers.bmpattern.ResultMessage.common_result

    def userLogin = Action (request => raq.requestArgs(request) { jv =>
        MessageRoutes(msg_log(toJson(Map("method" -> toJson("push"))), jv)
            :: msg_queryWXAuthPara(jv)
            :: msg_queryUser(jv)
            :: msg_pushUser(jv)
            :: msg_dropWXUnwanted(jv)
            :: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("db" -> dbt, "att" -> att))))
    })
}