package bmlogic.providers.ProvidersData

import com.mongodb.casbah.Imports._
import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson

trait result {
    implicit val d2m : DBObject => Map[String, JsValue] = { obj =>
        Map(
            "provider_id" -> toJson(obj.getAs[ObjectId]("_id").get.toString),
            "brand_name" -> toJson(obj.getAs[String]("brand_name").get),
            "short_name" -> toJson(obj.getAs[String]("short_name").get),
            "address" -> toJson(obj.getAs[String]("address").map (x => x).getOrElse("")),
            "service_type" -> toJson(obj.getAs[String]("service_type").map (x => x).getOrElse("")),
            "service_leaf" -> toJson(obj.getAs[String]("service_leaf").map (x => x).getOrElse("")),
            "logo" -> toJson(obj.getAs[String]("logo").get),
            "found_date" -> toJson(obj.getAs[String]("found_date").get),
            "onepunchline" -> toJson(obj.getAs[String]("onepunchline").get),
            "difference" -> toJson(obj.getAs[String]("difference").get),
            "story" -> toJson(obj.getAs[String]("story").get),
            "description" -> toJson(obj.getAs[String]("description").get),
            "date" -> toJson(obj.getAs[Number]("date").get.longValue)
        )
    }
}