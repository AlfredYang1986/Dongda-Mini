package bmlogic.answer.answerData

import com.mongodb.casbah.Imports._
import org.bson.types.ObjectId
import play.api.libs.json.JsValue

trait creation {
    implicit val m2d : JsValue => DBObject = { js =>
        val data = (js \ "answer").asOpt[JsValue].get

        val builder = MongoDBObject.newBuilder
        builder += "_id" -> ObjectId.get()
        builder += "description" -> (data \ "description").asOpt[String].get

        val lst_builder = MongoDBList.newBuilder
        val lst = (data \ "choice").asOpt[List[JsValue]].get
        (lst zipWithIndex) foreach { iter =>
            val tmp = MongoDBObject.newBuilder
            tmp += "index" -> iter._2
            tmp += "image" -> (iter._1 \ "image").asOpt[String].get
            tmp += "title" -> (iter._1 \ "title").asOpt[String].get

            lst_builder += tmp.result
        }

        builder += "random" -> (data \ "random").asOpt[Int].get
        builder += "choice" -> lst_builder.result
        builder += "answer" -> (data \ "answer").asOpt[Int].get

        builder.result
    }
}
