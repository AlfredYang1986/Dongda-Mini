package bmlogic.scroes

import play.api.libs.json.JsValue
import com.pharbers.bmmessages.CommonMessage

abstract class msg_ScoresCommand extends CommonMessage(cat ="scores", mt = ScoresModule)

object ScoresMessage {
    case class msg_addScores(data : JsValue) extends msg_ScoresCommand
    case class msg_addScoresIfNotExist(data : JsValue) extends msg_ScoresCommand
    case class msg_queryScores(data : JsValue) extends msg_ScoresCommand
    case class msg_preAnswerScores(data : JsValue) extends msg_ScoresCommand
    case class msg_postAnswerScores(data : JsValue) extends msg_ScoresCommand
    case class msg_postCheckInScores(data : JsValue) extends msg_ScoresCommand
}