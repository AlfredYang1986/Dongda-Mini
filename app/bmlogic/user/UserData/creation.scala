package bmlogic.user.UserData

import java.util.Date

import com.mongodb.casbah.Imports._
import org.bson.types.ObjectId
import play.api.libs.json.JsValue

trait creation {
    implicit val m2d : JsValue => DBObject = { js =>
        val data = (js \ "user").asOpt[JsValue].get

        val builder = MongoDBObject.newBuilder
        builder += "_id" -> ObjectId.get()
        builder += "name" -> (data \ "name").asOpt[String].map (x => x).getOrElse("")
        builder += "photo" -> (data \ "photo").asOpt[String].map (x => x).getOrElse("")
        builder += "wechat_id" ->
            (data \ "wechat_id").asOpt[String].map (x => x)
                .getOrElse((js \ "condition" \ "wechat_id").asOpt[String].get)
        builder += "date" -> new Date().getTime

        builder.result
    }
}
