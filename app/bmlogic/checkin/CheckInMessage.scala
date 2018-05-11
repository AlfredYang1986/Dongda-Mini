package bmlogic.checkin

import play.api.libs.json.JsValue
import com.pharbers.bmmessages.CommonMessage

abstract class msg_CheckInCommand extends CommonMessage(cat ="providers", mt = CheckInModule)

object CheckInMessage {
    case class msg_pushCheckIn(data : JsValue) extends msg_CheckInCommand
    case class msg_isChecked(data : JsValue) extends msg_CheckInCommand
}