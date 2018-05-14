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
        builder += "sd" -> (data \ "sd").asOpt[Long].get
        builder += "ed" -> (data \ "ed").asOpt[Long].get
        builder += "date" -> new Date().getTime

        builder.result
    }
}
