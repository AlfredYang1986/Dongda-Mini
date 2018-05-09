package bmlogic.provider.ProviderData

import com.mongodb.casbah.Imports._
import play.api.libs.json.JsValue

trait condition {
    implicit val qc : JsValue => DBObject = { js =>
        val id = (js \ "condition" \ "apply_id").asOpt[String].get
        DBObject("_id" -> new ObjectId(id))
    }

    implicit val sc : JsValue => DBObject = { js =>
        val con = (js \ "condition").asOpt[JsValue].get

        val builder = MongoDBObject.newBuilder
        (con \ "approved").asOpt[Int].map (x => builder += "approved" -> x).getOrElse(Unit)
        (con \ "wechat_open_id").asOpt[String].map (x => builder += "wechat.wechat_open_id" -> x).getOrElse(Unit)

//        val reVal = builder.result
//        if (reVal.isEmpty) DBObject("search" -> "null")
//        else reVal
        builder.result
    }
}
