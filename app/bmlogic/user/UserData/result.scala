package bmlogic.user.UserData

import com.mongodb.casbah.Imports._
import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson

trait result {
    implicit val d2m : DBObject => Map[String, JsValue] = { obj =>
        Map(
            "user_id" -> toJson(obj.getAs[ObjectId]("_id").get.toString),
            "name" -> toJson(obj.getAs[String]("name").get),
            "photo" -> toJson(obj.getAs[String]("photo").get),
            "wechat_id" -> toJson(obj.getAs[String]("wechat_id").get),
            "date" -> toJson(obj.getAs[Number]("date").get.longValue)
        )
    }
}
