package bmlogic.user

import play.api.libs.json.JsValue
import com.pharbers.bmmessages.CommonMessage

abstract class msg_UserCommand extends CommonMessage(cat ="user", mt = UserModule)

object UserMessage {
    case class msg_pushUserApply(data : JsValue) extends msg_UserCommand
    case class msg_popUserApply(data : JsValue) extends msg_UserCommand
    case class msg_queryUserApply(data : JsValue) extends msg_UserCommand
    case class msg_searchUserApply(data : JsValue) extends msg_UserCommand
}