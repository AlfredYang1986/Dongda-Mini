package bmlogic.user.UserData

import java.util.Date

import com.mongodb.casbah.Imports._
import org.bson.types.ObjectId
import play.api.libs.json.JsValue

trait creation {
    implicit val m2d : JsValue => DBObject = { js =>
        val data = (js \ "user").asOpt[JsValue].get

        val builder = MongoDBObject.newBuilder
        builder += "_id" -> ObjectId.get()
        builder += "name" -> (data \ "name").asOpt[String].map (x => x).getOrElse("")
        builder += "photo" -> (data \ "photo").asOpt[String].map (x => x).getOrElse("")
        builder += "wechat_id" ->
            (data \ "wechat_id").asOpt[String].map (x => x)
                .getOrElse((js \ "condition" \ "wechat_id").asOpt[String].get)

        val date = new Date().getTime
        builder += "date" -> date
        builder += "last" -> date

        builder.result
    }

    implicit val up2d : (DBObject, JsValue) => DBObject = { (obj, js) =>

        (js \ "name").asOpt[String].map (x => obj += "name" -> x).getOrElse(Unit)
        (js \ "photo").asOpt[String].map (x => obj += "photo" -> x).getOrElse(Unit)
        (js \ "last").asOpt[Long].map (x => obj += "last" -> x.asInstanceOf[Number]).getOrElse(Unit)

        obj
    }
}
