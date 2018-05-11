package bmlogic.user.UserData

import com.mongodb.casbah.Imports._
import play.api.libs.json.JsValue

trait condition {
    implicit val qc : JsValue => DBObject = { js =>
        val a =
            (js \ "condition" \ "user_id").asOpt[String].map { id =>
                Some(DBObject("_id" -> new ObjectId(id)))
            }.getOrElse(None)

        val b =
            (js \ "condition" \ "wechat_id").asOpt[String].map { id =>
                Some(DBObject("wechat_id" -> id))
            }.getOrElse(None)

        (a :: b :: Nil).filter(_ != None).map (_.get) match {
            case xls : List[DBObject] => $and(xls)
            case Nil => DBObject("search" -> "null")
        }
    }

    implicit val sc : JsValue => DBObject = { js =>
        val con = (js \ "condition").asOpt[JsValue].get

        val builder = MongoDBObject.newBuilder
        (con \ "wechat_open_id").asOpt[String].map (x => builder += "wechat.wechat_open_id" -> x).getOrElse(Unit)

        //        val reVal = builder.result
        //        if (reVal.isEmpty) DBObject("search" -> "null")
        //        else reVal
        builder.result
    }
}
