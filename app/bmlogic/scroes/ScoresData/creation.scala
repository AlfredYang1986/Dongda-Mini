package bmlogic.scroes.ScoresData

import com.mongodb.casbah.Imports._
import org.bson.types.ObjectId
import play.api.libs.json.JsValue

trait creation {
    implicit val m2d : JsValue => DBObject = { js =>
        val data = js

        val builder = MongoDBObject.newBuilder
        builder += "_id" -> ObjectId.get()

        val (a, b, c) =
            (js \ "level").asOpt[String].map (x => x).getOrElse("scores_A") match {
                case "scores_A" => (1, 0, 0)
                case "scores_B" => (0, 1, 0)
                case "scores_C" => (0, 0, 1)
            }

        builder += "scores_A" -> a
        builder += "scores_B" -> b
        builder += "scores_C" -> c
        builder += "user_id" -> (data \ "user" \ "user_id").asOpt[String].get

        builder.result
    }

    implicit val up2d : (JsValue, DBObject) => DBObject = { (js, obj) =>
        val data = js
        assert(obj.getAs[String]("user_id").get == (data \ "user" \ "user_id").asOpt[String].get)

        val (a, b, c) =
            (js \ "level").asOpt[String].map (x => x).getOrElse("scores_A") match {
                case "scores_A" => (1, 0, 0)
                case "scores_B" => (0, 1, 0)
                case "scores_C" => (0, 0, 1)
            }

        val tmp_A = obj.getAs[Number]("scores_A").get.intValue + a
        val tmp_B = obj.getAs[Number]("scores_B").get.intValue + b
        val tmp_C = obj.getAs[Number]("scores_C").get.intValue + c
        obj += "scores_A" -> tmp_A.asInstanceOf[Number]
        obj += "scores_B" -> tmp_B.asInstanceOf[Number]
        obj += "scores_C" -> tmp_C.asInstanceOf[Number]
        obj
    }
}
