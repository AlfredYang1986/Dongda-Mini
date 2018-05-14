package bmlogic.scroes.ScoresData

import com.mongodb.casbah.Imports._
import play.api.libs.json.JsValue

trait condition {
    implicit val qc : JsValue => DBObject = { js =>
        val id = (js \ "scores" \ "scores_id").asOpt[String].get
        DBObject("_id" -> new ObjectId(id))
    }

    implicit val sc : JsValue => DBObject = { js =>
        val user_id = (js \ "condition" \ "user_id").asOpt[String].
                            map (x => x).getOrElse((js \ "user" \ "user_id").asOpt[String].get)

        val builder = MongoDBObject.newBuilder
        builder += "user_id" -> user_id

        builder.result
    }
}
