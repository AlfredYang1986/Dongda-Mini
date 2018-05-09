package bmlogic.user.UserData

import com.mongodb.casbah.Imports._
import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson

trait result {
    implicit val d2m : DBObject => Map[String, JsValue] = { obj =>

        val wechat = obj.getAs[BasicDBObject]("wechat").get
        val wm =
            Map(
                "wechat_name" -> toJson(wechat.getAs[String]("wechat_name").get),
                "wechat_photo" -> toJson(wechat.getAs[String]("wechat_photo").get),
                "wechat_open_id" -> toJson(wechat.getAs[String]("wechat_open_id").get),
                "wechat_token" -> toJson(wechat.getAs[String]("wechat_token").get)
            )

//        val ab = obj.getAs[BasicDBObject]("age_boundary").get
//        val abm =
//            Map(
//                "low" -> toJson(ab.getAs[Number]("low").get.intValue),
//                "up" -> toJson(ab.getAs[Number]("up").get.intValue)
//            )

        Map(
            "apply_id" -> toJson(obj.getAs[ObjectId]("_id").get.toString),
//            "contact" -> toJson(obj.getAs[String]("contact").get.toString),
//            "contact_no" -> toJson(obj.getAs[String]("contact_no").get.toString),
//            "brand_name" -> toJson(obj.getAs[String]("brand_name").get),
//            "service_type" -> toJson(obj.getAs[String]("service_type").map (x => x).getOrElse("")),
//            "service_leaf" -> toJson(obj.getAs[String]("service_leaf").map (x => x).getOrElse("")),
//            "input_pwd" -> toJson(obj.getAs[String]("input_pwd").map (x => x).getOrElse("")),
//            "address" -> toJson(obj.getAs[String]("address").map (x => x).getOrElse("")),
            "wechat_user" -> toJson(wm),
//            "age_boundary" -> toJson(abm),
            "approved" -> toJson(obj.getAs[Number]("approved").get.intValue),
            "date" -> toJson(obj.getAs[Number]("date").get.longValue)
        )
    }
}
