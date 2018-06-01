package bmlogic.providerslevel

import play.api.libs.json.JsValue
import com.pharbers.bmmessages.CommonMessage

abstract class msg_ProvidersLevelCommand extends CommonMessage(cat ="providers", mt = ProvidersLevelModule)

object ProvidersLevelMessage {
    case class msg_pushProvidersLevel(data : JsValue) extends msg_ProvidersLevelCommand
    case class msg_queryProvidersLevel(data : JsValue) extends msg_ProvidersLevelCommand

    case class msg_queryCollectedProviders(data : JsValue) extends msg_ProvidersLevelCommand
    case class msg_queryTopProviders(data : JsValue) extends msg_ProvidersLevelCommand

    case class msg_queryDisplayAges(data : JsValue) extends msg_ProvidersLevelCommand
    case class msg_mergeDisplayAges(data : JsValue) extends msg_ProvidersLevelCommand

    case class msg_queryServiceDate(data : JsValue) extends msg_ProvidersLevelCommand
    case class msg_mergeServiceDate(data : JsValue) extends msg_ProvidersLevelCommand

    case class msg_queryDisplayTimes(data : JsValue) extends msg_ProvidersLevelCommand
    case class msg_mergeDisplayTimes(data : JsValue) extends msg_ProvidersLevelCommand


    case class msg_resetLevels(data : JsValue) extends msg_ProvidersLevelCommand
}