package bmlogic.providerslevel.ProvidersLevelData

import java.util.Date

import com.mongodb.casbah.Imports._
import play.api.libs.json.JsValue

trait condition {
    implicit val qc : JsValue => DBObject = { js =>
        val id = (js \ "condition" \ "level_id").asOpt[String].get
        DBObject("_id" -> new ObjectId(id))
    }

    implicit val msc : JsValue => DBObject = { js =>
//        val date = new Date().getTime
        val providers = (js \ "condition" \ "providers").asOpt[List[String]].get

        providers match {
            case Nil => DBObject("search" -> "null")
            case xls : List[String] => $or(xls map (x => DBObject("provider_id" -> x)))
        }
    }

    implicit val sc : JsValue => DBObject = { js =>

        val date = new Date().getTime
        val provider_id = (js \ "condition" \ "provider_id").asOpt[String].map (x => x).getOrElse ((js \ "provider" \ "provider_id").asOpt[String].get)

        val builder = MongoDBObject.newBuilder
        builder += "provider_id" -> provider_id

        $and(("sd" $lte date) :: ("ed" $gte date) :: builder.result :: Nil)
    }

    implicit val soc : JsValue => DBObject = { js =>

        val provider_id = (js \ "condition" \ "provider_id").asOpt[String].map (x => x).getOrElse ((js \ "provider" \ "provider_id").asOpt[String].get)

        val builder = MongoDBObject.newBuilder
        builder += "provider_id" -> provider_id

        builder.result
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

    implicit val ttc : JsValue => DBObject = { js =>
        val date = new Date().getTime
        $and(("sd" $lte date) :: ("ed" $gte date) :: Nil)
    }
}
