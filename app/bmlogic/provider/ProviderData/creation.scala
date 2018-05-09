package bmlogic.provider.ProviderData

import java.util.Date

import play.api.libs.json.JsValue
import com.mongodb.casbah.Imports._
import org.bson.types.ObjectId

trait creation {
    implicit val m2d : JsValue => DBObject = { js =>
        val data = (js \ "apply").asOpt[JsValue].get
        val wechat_user = (data \ "wechat_user").asOpt[JsValue].get

        val wechat_builder = MongoDBObject.newBuilder
        wechat_builder += "wechat_name" -> (wechat_user \ "wechat_name").asOpt[String].get
        wechat_builder += "wechat_photo" -> (wechat_user \ "wechat_photo").asOpt[String].get
        wechat_builder += "wechat_open_id" -> (wechat_user \ "wechat_open_id").asOpt[String].get
        wechat_builder += "wechat_token" -> (wechat_user \ "wechat_token").asOpt[String].get

        val age_boundary = MongoDBObject.newBuilder
        age_boundary += "low" -> (data \ "age_boundary" \ "low").asOpt[Int].get
        age_boundary += "up" -> (data \ "age_boundary" \ "up").asOpt[Int].get

        val builder = MongoDBObject.newBuilder
        builder += "_id" -> ObjectId.get()
        builder += "age_boundary" -> age_boundary.result
        builder += "wechat" -> wechat_builder.result
        builder += "address" -> (data \ "address").asOpt[String].map(x => x).getOrElse("")
        builder += "contact" -> (data \ "contact").asOpt[String].map(x => x).getOrElse("")
        builder += "contact_no" -> (data \ "contact_no").asOpt[String].map(x => x).getOrElse("")
        builder += "brand_name" -> (data \ "brand_name").asOpt[String].map(x => x).getOrElse("")
        builder += "service_type" -> (data \ "service_type").asOpt[String].map(x => x).getOrElse("")
        builder += "service_leaf" -> (data \ "service_leaf").asOpt[String].map(x => x).getOrElse("")
        builder += "input_pwd" -> (data \ "input_pwd").asOpt[String].map (x => x).getOrElse("")
        builder += "approved" -> 0
        builder += "date" -> new Date().getTime

        builder.result
    }
}
