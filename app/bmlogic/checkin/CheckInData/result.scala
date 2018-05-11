package bmlogic.checkin.CheckInData

import com.mongodb.casbah.Imports._
import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson

trait result {
    implicit val d2m : DBObject => Map[String, JsValue] = { obj =>
        Map(
            "checkin_id" -> toJson(obj.getAs[ObjectId]("_id").get.toString),
            "provider_id" -> toJson(obj.getAs[String]("provider_id").get.toString),
            "user_id" -> toJson(obj.getAs[String]("user_id").get.toString),
            "date" -> toJson(obj.getAs[Number]("date").get.longValue)
        )
    }
}
