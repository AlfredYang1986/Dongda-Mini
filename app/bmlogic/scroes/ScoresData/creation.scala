package bmlogic.scroes.ScoresData

import com.mongodb.casbah.Imports._
import org.bson.types.ObjectId
import play.api.libs.json.JsValue

trait creation {
    implicit val m2d : JsValue => DBObject = { js =>
        val data = js

        val builder = MongoDBObject.newBuilder
        builder += "_id" -> ObjectId.get()

        builder += "scores_A" -> (data \ "scores_A").asOpt[Int].map (x => x.asInstanceOf[Number]).getOrElse(1.asInstanceOf[Number])
        builder += "scores_B" -> (data \ "scores_B").asOpt[Int].map (x => x.asInstanceOf[Number]).getOrElse(0.asInstanceOf[Number])
        builder += "scores_C" -> (data \ "scores_C").asOpt[Int].map (x => x.asInstanceOf[Number]).getOrElse(0.asInstanceOf[Number])
        builder += "user_id" -> (data \ "user" \ "user_id").asOpt[String].get

        builder.result
    }

    implicit val up2d : (JsValue, DBObject) => DBObject = { (js, obj) =>
        val data = js
        assert(obj.getAs[String]("user_id").get == (data \ "user" \ "user_id").asOpt[String].get)
        val tmp_A = (obj.getAs[Number]("scores_A").get.intValue + (data \ "scores" \ "scores_A").asOpt[Int].get.intValue)
        val tmp_B = (obj.getAs[Number]("scores_B").get.intValue + (data \ "scores" \ "scores_B").asOpt[Int].get.intValue)
        val tmp_C = (obj.getAs[Number]("scores_C").get.intValue + (data \ "scores" \ "scores_C").asOpt[Int].get.intValue)
        obj += "scores_A" -> tmp_A.asInstanceOf[Number]
        obj += "scores_B" -> tmp_B.asInstanceOf[Number]
        obj += "scores_C" -> tmp_C.asInstanceOf[Number]
        obj
    }
}
