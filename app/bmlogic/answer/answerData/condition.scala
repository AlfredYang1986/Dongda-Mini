package bmlogic.answer.answerData

import com.mongodb.casbah.Imports._
import play.api.libs.json.JsValue

trait condition {
    implicit val qc : JsValue => DBObject = { js =>
        val answer_id = (js \ "condition" \ "answer_id").asOpt[String].get
        DBObject("_id" -> new ObjectId(answer_id))
    }

    implicit val mqc : JsValue => DBObject = { js =>
        val lst = (js \ "condition" \ "answers").asOpt[List[String]].get
        lst match {
            case Nil => DBObject("search" -> "null")
            case xls : List[String] => $or(xls map (x => DBObject("_id" -> new ObjectId(x))))
        }
    }

    implicit val rqc : JsValue => DBObject = { js =>
//        val lst = (js \ "condition" \ "random").asOpt[List[String]].get
        val lst = (js \ "random").asOpt[List[Int]].get
        lst match {
            case Nil => DBObject("search" -> "null")
            case xls : List[Int] => $or(xls map (x => DBObject("random" -> x)))
        }
    }

}
