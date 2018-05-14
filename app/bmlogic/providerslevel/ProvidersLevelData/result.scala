package bmlogic.providerslevel.ProvidersLevelData

import com.mongodb.casbah.Imports._
import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson

trait result {
    implicit val d2m : DBObject => Map[String, JsValue] = { obj =>
        Map(
            "level_id" -> toJson(obj.getAs[ObjectId]("_id").get.toString),
            "ll" -> toJson(obj.getAs[String]("ll").get)
        )
    }
}