package bmlogic.user

import play.api.libs.json.JsValue
import com.pharbers.bmmessages.CommonMessage

abstract class msg_UserCommand extends CommonMessage(cat ="user", mt = UserModule)

object UserMessage {
    case class msg_pushUser(data : JsValue) extends msg_UserCommand
    case class msg_popUser(data : JsValue) extends msg_UserCommand
    case class msg_queryUser(data : JsValue) extends msg_UserCommand
    case class msg_searchUsers(data : JsValue) extends msg_UserCommand

    case class msg_lastLoginTime(data : JsValue) extends msg_UserCommand
    case class msg_updateLoginTime(data : JsValue) extends msg_UserCommand
    case class msg_unWantedLogin(data : JsValue) extends msg_UserCommand
}