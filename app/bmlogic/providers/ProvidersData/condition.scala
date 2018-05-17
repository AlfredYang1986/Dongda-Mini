package bmlogic.providers.ProvidersData

import java.net.URLDecoder

import com.mongodb.casbah.Imports._
import play.api.libs.json.JsValue

trait condition {
    implicit val qc : JsValue => DBObject = { js =>
        val id = (js \ "condition" \ "provider_id").asOpt[String].get
        DBObject("_id" -> new ObjectId(id))
    }

    implicit val mc : JsValue => DBObject = { js =>
        val lst = (js \ "condition" \ "providers").asOpt[List[String]].get
        println(lst)
        lst match {
            case Nil => DBObject("search" -> "null")
            case xls : List[String] => $or(xls map (x => DBObject("_id" -> new ObjectId(x))))
        }
    }

    implicit val sc : JsValue => DBObject = { js =>
        val con = (js \ "condition").asOpt[JsValue].get

        val brand_name = (con \ "brand_name").asOpt[String].get
        val tmp = URLDecoder.decode(brand_name, "UTF-8")
        val builder = MongoDBObject.newBuilder
        builder += "brand_name" -> tmp

        builder.result
    }

    implicit val asc : JsValue => DBObject = { js =>
//        val con = (js \ "condition").asOpt[JsValue].get
        val builder = MongoDBObject.newBuilder
        builder.result
    }
}
