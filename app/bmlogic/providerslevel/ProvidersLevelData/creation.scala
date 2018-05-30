package bmlogic.providerslevel.ProvidersLevelData

import java.util.Date

import com.mongodb.casbah.Imports._
import org.bson.types.ObjectId
import play.api.libs.json.JsValue

trait creation {
    implicit val m2d : JsValue => DBObject = { js =>
        val data = (js \ "level").asOpt[JsValue].get

        val builder = MongoDBObject.newBuilder
        builder += "_id" -> ObjectId.get()
        builder += "provider_id" -> (data \ "provider_id").asOpt[String].get
        builder += "ll" -> (data \ "ll").asOpt[String].get
        builder += "is_collected" -> (data \ "is_collected").asOpt[Int].get
        builder += "is_top" -> (data \ "is_top").asOpt[Int].get
        builder += "sd" -> (data \ "sd").asOpt[Long].get
        builder += "ed" -> (data \ "ed").asOpt[Long].get
        builder += "ssd" -> (data \ "ssd").asOpt[Long].map (x => x).getOrElse(0L)
        builder += "sed" -> (data \ "sed").asOpt[Long].map (x => x).getOrElse(0L)
        builder += "age" -> (data \ "age").asOpt[String].get
        builder += "date" -> new Date().getTime

        builder.result
    }
}
