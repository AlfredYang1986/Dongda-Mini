package bmlogic.answer

import play.api.libs.json.JsValue
import com.pharbers.bmmessages.CommonMessage

abstract class msg_AnswerCommand extends CommonMessage(cat ="answer", mt = answerModule)

object answerMessage {
    case class msg_pushAnswer(data : JsValue) extends msg_AnswerCommand
    case class msg_randomAnswers(data : JsValue) extends msg_AnswerCommand
    case class msg_checkAnswers(data : JsValue) extends msg_AnswerCommand
    case class msg_randomGenerator(data : JsValue) extends msg_AnswerCommand
}