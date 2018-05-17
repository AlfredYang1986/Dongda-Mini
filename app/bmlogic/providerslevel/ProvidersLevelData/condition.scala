package bmlogic.providerslevel.ProvidersLevelData

import java.util.Date

import com.mongodb.casbah.Imports._
import play.api.libs.json.JsValue

trait condition {
    implicit val qc : JsValue => DBObject = { js =>
        val id = (js \ "condition" \ "level_id").asOpt[String].get
        DBObject("_id" -> new ObjectId(id))
    }

    implicit val sc : JsValue => DBObject = { js =>

        val date = new Date().getTime
        val provider_id = (js \ "condition" \ "provider_id").asOpt[String].map (x => x).getOrElse ((js \ "provider" \ "provider_id").asOpt[String].get)

        val builder = MongoDBObject.newBuilder
        builder += "provider_id" -> provider_id

        $and(("sd" $lte date) :: ("ed" $gte date) :: builder.result :: Nil)
    }

    implicit val spc : JsValue => DBObject = { js =>

        val date = new Date().getTime
        val builder = MongoDBObject.newBuilder
        builder += "is_collected" -> 1
        $and(("sd" $lte date) :: ("ed" $gte date) :: builder.result :: Nil)
    }

    implicit val tpc : JsValue => DBObject = { js =>

        val date = new Date().getTime
        val builder = MongoDBObject.newBuilder
        builder += "is_top" -> 1
        $and(("sd" $lte date) :: ("ed" $gte date) :: builder.result :: Nil)
    }
}
