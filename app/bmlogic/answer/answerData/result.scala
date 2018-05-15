package bmlogic.answer.answerData

import com.mongodb.casbah.Imports._
import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson

trait result {
    implicit val dr : DBObject => Map[String, JsValue] = { obj =>

        val choice = obj.getAs[MongoDBList]("choice").get.toList.asInstanceOf[List[BasicDBObject]].
            map (x => toJson(Map(
                "index" -> toJson(x.getAs[Number]("index").get.intValue),
                "image" -> toJson(x.getAs[String]("image").get),
                "title" -> toJson(x.getAs[String]("title").get)
            )))

        Map(
            "answer_id" -> toJson(obj.getAs[ObjectId]("_id").get.toString),
            "description" -> toJson(obj.getAs[String]("description").get.toString),
            "choice" -> toJson(choice)
//            "answer" -> toJson(obj.getAs[Number]("answer").get.longValue)
        )
    }

    implicit val sr : DBObject => Map[String, JsValue] = { obj =>
        Map(
            "answer_id" -> toJson(obj.getAs[ObjectId]("_id").get.toString),
            "answer" -> toJson(obj.getAs[Number]("answer").get.longValue)
        )
    }
}
