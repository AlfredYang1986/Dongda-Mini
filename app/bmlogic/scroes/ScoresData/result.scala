package bmlogic.scroes.ScoresData

import com.mongodb.casbah.Imports._
import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson

trait result {
    implicit val d2m : DBObject => Map[String, JsValue] = { obj =>
        Map(
            "scores_id" -> toJson(obj.getAs[ObjectId]("_id").get.toString),
            "scores_A" -> toJson(obj.getAs[Number]("scores_A").get.intValue),
            "scores_B" -> toJson(obj.getAs[Number]("scores_B").get.intValue),
            "scores_C" -> toJson(obj.getAs[Number]("scores_C").get.intValue),
            "scores_D" -> toJson(obj.getAs[Number]("scores_D").map (x => x.intValue).getOrElse(0)),
            "user_id" -> toJson(obj.getAs[String]("user_id").get)
        )
    }
}
