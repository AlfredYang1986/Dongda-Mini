package bmlogic.checkin.CheckInData

import java.util.Date

import com.mongodb.casbah.Imports._
import org.bson.types.ObjectId
import play.api.libs.json.JsValue

trait creation {
    implicit val m2d : JsValue => DBObject = { data =>
//        val data = (js \ "checkin").asOpt[JsValue].get
//        val data = (js \ "checkin").asOpt[JsValue].get

        val builder = MongoDBObject.newBuilder
        builder += "_id" -> ObjectId.get()
        builder += "provider_id" -> (data \ "provider" \ "provider_id").asOpt[String].get
        builder += "user_id" -> (data \ "user" \ "user_id").asOpt[String].get
        builder += "date" -> new Date().getTime

        builder.result
    }
}
