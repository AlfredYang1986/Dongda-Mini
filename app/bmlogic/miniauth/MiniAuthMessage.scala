package bmlogic.miniauth

import play.api.libs.json.JsValue
import com.pharbers.bmmessages.CommonMessage

abstract class msg_MiniAuthCommand extends CommonMessage(cat ="providers", mt = MiniAuthModule)

object MiniAuthMessage {
    case class msg_queryWXAuthPara(data : JsValue) extends msg_MiniAuthCommand
    case class msg_dropWXUnwanted(data : JsValue) extends msg_MiniAuthCommand
}
