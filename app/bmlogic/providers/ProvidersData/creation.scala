package bmlogic.providers.ProvidersData

import java.util.Date

import com.mongodb.casbah.Imports._
import org.bson.types.ObjectId
import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson

trait creation {
    implicit val m2d : JsValue => DBObject = { js =>
        val data = (js \ "provider").asOpt[JsValue].get

        val builder = MongoDBObject.newBuilder
        builder += "_id" -> ObjectId.get()
        builder += "brand_name" -> (data \ "brand_name").asOpt[String].map(x => x).getOrElse("")
        builder += "short_name" -> (data \ "short_name").asOpt[String].map(x => x).getOrElse("")
        builder += "address" -> (data \ "address").asOpt[String].map(x => x).getOrElse("")
        builder += "service_type" -> (data \ "service_type").asOpt[String].map(x => x).getOrElse("")
        builder += "service_leaf" -> (data \ "service_leaf").asOpt[String].map(x => x).getOrElse("")
        builder += "logo" -> (data \ "logo").asOpt[String].map(x => x).getOrElse("")
        builder += "found_date" -> (data \ "found_date").asOpt[String].map(x => x).getOrElse("")
        builder += "onepunchline" -> (data \ "onepunchline").asOpt[String].map(x => x).getOrElse("")
//        builder += "difference" -> (data \ "difference").asOpt[String].map(x => x).getOrElse("")
        builder += "difference" -> (data \ "difference").asOpt[List[String]].map(x => x).getOrElse("")
        builder += "story" -> (data \ "story").asOpt[String].map(x => x).getOrElse("")
        builder += "description" -> (data \ "description").asOpt[String].map(x => x).getOrElse("")
        builder += "isPaid" -> (data \ "isPaid").asOpt[Int].map(x => x).getOrElse(0)
        builder += "festival" -> (data \ "festival").asOpt[String].map (x => x).getOrElse("")
        builder += "date" -> new Date().getTime

        builder.result
    }
}
