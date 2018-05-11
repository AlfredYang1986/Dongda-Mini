package bmlogic.providers
import play.api.libs.json.JsValue
import com.pharbers.bmmessages.CommonMessage

abstract class msg_ProvidersCommand extends CommonMessage(cat ="providers", mt = ProvidersModule)

object ProvidersMessage {
    case class msg_pushProvider(data : JsValue) extends msg_ProvidersCommand
    case class msg_popProvider(data : JsValue) extends msg_ProvidersCommand
    case class msg_queryProvider(data : JsValue) extends msg_ProvidersCommand
    case class msg_queryProviderMulti(data : JsValue) extends msg_ProvidersCommand
    case class msg_searchProvider(data : JsValue) extends msg_ProvidersCommand
    case class msg_queryProviderOne(data : JsValue) extends msg_ProvidersCommand
}
