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

    case class msg_mergeCheckedProvider(data : JsValue) extends msg_ProvidersCommand
    case class msg_mergeCheckedProviderOne(data : JsValue) extends msg_ProvidersCommand

    case class msg_mergeTopProvider(data : JsValue) extends msg_ProvidersCommand
    case class msg_mergeTopProviderOne(data : JsValue) extends msg_ProvidersCommand

    case class msg_dropUnwantedMessage(data : JsValue) extends msg_ProvidersCommand

    case class msg_resetProviderLogo(data : JsValue) extends msg_ProvidersCommand
    case class msg_resetLocationPin(data : JsValue) extends msg_ProvidersCommand
    case class msg_resetProviderSearchId(data :JsValue) extends msg_ProvidersCommand
    case class msg_exportProviders(data : JsValue) extends msg_ProvidersCommand
}
