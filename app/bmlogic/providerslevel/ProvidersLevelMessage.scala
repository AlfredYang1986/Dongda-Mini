package bmlogic.providerslevel

import play.api.libs.json.JsValue
import com.pharbers.bmmessages.CommonMessage

abstract class msg_ProvidersLevelCommand extends CommonMessage(cat ="providers", mt = ProvidersLevelModule)

object ProvidersLevelMessage {
    case class msg_pushProvidersLevel(data : JsValue) extends msg_ProvidersLevelCommand
    case class msg_queryProvidersLevel(data : JsValue) extends msg_ProvidersLevelCommand

    case class msg_queryCollectedProviders(data : JsValue) extends msg_ProvidersLevelCommand
}