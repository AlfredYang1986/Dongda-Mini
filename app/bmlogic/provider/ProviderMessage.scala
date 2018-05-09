package bmlogic.provider

import play.api.libs.json.JsValue
import com.pharbers.bmmessages.CommonMessage

abstract class msg_ProviderCommand extends CommonMessage(cat ="provider", mt = ProviderModule)

object ProviderMessage {
    case class msg_pushProviderApply(data : JsValue) extends msg_ProviderCommand
    case class msg_popProviderApply(data : JsValue) extends msg_ProviderCommand
    case class msg_queryProviderApply(data : JsValue) extends msg_ProviderCommand
    case class msg_searchProviderApply(data : JsValue) extends msg_ProviderCommand
}