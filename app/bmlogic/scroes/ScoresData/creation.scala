package bmlogic.scroes.ScoresData

import com.mongodb.casbah.Imports._
import org.bson.types.ObjectId
import play.api.libs.json.JsValue

/**
  * A 是扫码就得
  * B 是扫码得答题得机会
  * C 是答题正确得惊喜
  * D 是扫码得高级蛋
  * E 抖音高级机会，得到方法不确定
  */

trait creation {
    implicit val m2d : JsValue => DBObject = { js =>
        val data = js

        val builder = MongoDBObject.newBuilder
        builder += "_id" -> ObjectId.get()

        val (a, b, c, d, e) =
            (js \ "level").asOpt[String].map (x => x).getOrElse("scores_A") match {
                case "scores_A" => (1, 0, 0, 0, 0)
                case "scores_B" => (0, 1, 0, 0, 0)
                case "scores_C" => (0, 0, 1, 0, 0)
                case "scores_D" => (0, 0, 0, 1, 0)
                case "scores_E" => (0, 0, 0, 0, 1)
            }

        builder += "scores_A" -> a
        builder += "scores_B" -> b
        builder += "scores_C" -> c
        builder += "scores_D" -> d
        builder += "scores_E" -> e
        builder += "user_id" -> (data \ "user" \ "user_id").asOpt[String].get

        builder.result
    }

    implicit val up2d : (JsValue, DBObject) => DBObject = { (js, obj) =>
        val data = js
        assert(obj.getAs[String]("user_id").get == (data \ "user" \ "user_id").asOpt[String].get)

        val (a, b, c, d, e) =
            (js \ "level").asOpt[String].map (x => x).getOrElse("scores_A") match {
                case "scores_A" => (1, 0, 0, 0, 0)
                case "scores_B" => (0, 1, 0, 0, 0)
                case "scores_C" => (0, 0, 1, 0, 0)
                case "scores_D" => (0, 0, 0, 1, 0)
                case "scores_E" => (0, 0, 0, 0, 1)
            }

        val tmp_A = obj.getAs[Number]("scores_A").get.intValue + a
        val tmp_B = obj.getAs[Number]("scores_B").get.intValue + b
        val tmp_C = obj.getAs[Number]("scores_C").get.intValue + c
        val tmp_D = obj.getAs[Number]("scores_D").get.intValue + d
        val tmp_E = obj.getAs[Number]("scores_E").get.intValue + e
        obj += "scores_A" -> tmp_A.asInstanceOf[Number]
        obj += "scores_B" -> tmp_B.asInstanceOf[Number]
        obj += "scores_C" -> tmp_C.asInstanceOf[Number]
        obj += "scores_D" -> tmp_D.asInstanceOf[Number]
        obj += "scores_E" -> tmp_E.asInstanceOf[Number]
        obj
    }

    implicit val mb2d : (JsValue, DBObject) => DBObject = { (js, obj) =>
        val data = js
        assert(obj.getAs[String]("user_id").get == (data \ "user" \ "user_id").asOpt[String].get)

        val tmp_B = obj.getAs[Number]("scores_B").get.intValue - 1
        obj += "scores_B" -> tmp_B.asInstanceOf[Number]
        obj
    }

    implicit val ab2d : (JsValue, DBObject) => DBObject = { (js, obj) =>
        val data = js
        assert(obj.getAs[String]("user_id").get == (data \ "user" \ "user_id").asOpt[String].get)

        val tmp_B = obj.getAs[Number]("scores_B").get.intValue + 1
        obj += "scores_B" -> tmp_B.asInstanceOf[Number]
        obj
    }

    implicit val ac2d : (JsValue, DBObject) => DBObject = { (js, obj) =>
        val data = js
        assert(obj.getAs[String]("user_id").get == (data \ "user" \ "user_id").asOpt[String].get)

        val tmp_C = obj.getAs[Number]("scores_C").get.intValue + 1
        obj += "scores_C" -> tmp_C.asInstanceOf[Number]
        obj
    }
}
