package bmlogic.checkin.CheckInData

import com.mongodb.casbah.Imports._
import play.api.libs.json.JsValue

trait condition {

    implicit val sc : JsValue => DBObject = { js =>
        val a =
            (js \ "provider" \ "provider_id").asOpt[String].map { ida =>
                Some(DBObject("provider_id" -> ida))
            }.getOrElse(None)

        val b =
            (js \ "user" \ "user_id").asOpt[String].map { idb =>
                Some(DBObject("user_id" -> idb))
            }.getOrElse(None)

        (a :: b :: Nil).filter (_ != None). map (x => x.get) match {
            case Nil => DBObject("search" -> "null")
            case xls : List[DBObject] => $and(xls)
        }
    }
}
