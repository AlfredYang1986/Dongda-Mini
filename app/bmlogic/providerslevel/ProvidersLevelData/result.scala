package bmlogic.providerslevel.ProvidersLevelData

import com.mongodb.casbah.Imports._
import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson

trait result {
    implicit val sr : DBObject => Map[String, JsValue] = { obj =>
        Map(
            "level_id" -> toJson(obj.getAs[ObjectId]("_id").get.toString),
            "ll" -> toJson(obj.getAs[String]("ll").get)
        )
    }

    implicit val colr : DBObject => Map[String, JsValue] = { obj =>
        Map(
            "level_id" -> toJson(obj.getAs[ObjectId]("_id").get.toString),
            "provider_id" -> toJson(obj.getAs[String]("provider_id").get.toString),
            "age" -> toJson(obj.getAs[String]("age").map (x => x).getOrElse("")),
            "is_collected" -> toJson(obj.getAs[Number]("is_collected").get.intValue)
        )
    }
}
