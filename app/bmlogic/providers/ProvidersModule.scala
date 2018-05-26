package bmlogic.providers

import java.io.{File, FileInputStream, FileOutputStream}
import java.util.UUID

import bmlogic.common.mergestepresult.MergeStepResult
import bmlogic.providers.ProvidersData._
import bmlogic.providers.ProvidersMessage._
import com.mongodb.BasicDBObject
import com.mongodb.casbah.Imports.{DBObject, ObjectId}
import com.mongodb.casbah.commons.MongoDBObject
import com.pharbers.ErrorCode
import com.pharbers.bmmessages.{CommonModules, MessageDefines}
import com.pharbers.bmpattern.ModuleTrait
import com.pharbers.dbManagerTrait.dbInstanceManager
import play.api.libs.json.{JsObject, JsValue}
import play.api.libs.json.Json.toJson

object ProvidersModule extends ModuleTrait {

    def dispatchMsg(msg: MessageDefines)(pr: Option[Map[String, JsValue]])(implicit cm: CommonModules): (Option[Map[String, JsValue]], Option[JsValue]) = msg match {
        case msg_pushProvider(data) => pushProvider(data)
        case msg_popProvider(data) => popProvider(data)
        case msg_queryProvider(data) => queryProvider(data)(pr)
        case msg_queryProviderMulti(data) => queryProviderMulti(data)(pr)
        case msg_queryProviderOne(data) => queryProviderOne(data)(pr)
        case msg_searchProvider(data) => searchProviders(data)(pr)
        case msg_mergeCheckedProvider(data) => mergeCheckedProvider(data)(pr)
        case msg_mergeCheckedProviderOne(data) => mergeCheckedProviderOne(data)(pr)
        case msg_dropUnwantedMessage(data) => dropUnwantedMessage(data)(pr)

        case msg_mergeTopProvider(data) => mergeTopProviders(data)(pr)
        case msg_mergeTopProviderOne(data) => mergeTopProvidersOne(data)(pr)

        case msg_resetProviderLogo(data) => resetProviderLogo(data)
        case _ => ???
    }

    object inner_traits extends creation with condition with result
    val logo_path = "logo/"
    val dst = "images/"

    def pushProvider(data : JsValue)
                    (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {
        try {
            val conn = cm.modules.get.get("db").map(x => x.asInstanceOf[dbInstanceManager]).getOrElse(throw new Exception("no db connection"))
            val db = conn.queryDBInstance("cli").get

            import inner_traits.m2d
            val o : DBObject = data
            db.insertObject(o, "providers", "_id")
            val reVal = o.get("_id").asInstanceOf[ObjectId].toString

            (Some(Map("provider_id" -> toJson(reVal))), None)

        } catch {
            case ex : Exception => println(s"push.error=${ex.getMessage}");(None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }

    def popProvider(data : JsValue)
                    (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {
        try {
            val conn = cm.modules.get.get("db").map(x => x.asInstanceOf[dbInstanceManager]).getOrElse(throw new Exception("no db connection"))
            val db = conn.queryDBInstance("cli").get

            import inner_traits.qc
            val o : DBObject = data
            db.deleteObject(o, "providers", "_id")

            (Some(Map("pop provider" -> toJson("success"))), None)

        } catch {
            case ex : Exception => println(s"push.error=${ex.getMessage}");(None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }

    def queryProvider(data : JsValue)
                     (pr : Option[Map[String, JsValue]])
                     (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {

        try {
            val conn = cm.modules.get.get("db").map(x => x.asInstanceOf[dbInstanceManager]).getOrElse(throw new Exception("no db connection"))
            val db = conn.queryDBInstance("cli").get

            val js = MergeStepResult(data, pr)
            val m = pr.map (x => x).getOrElse(Map.empty)

            import inner_traits.qc
            import inner_traits.d2m
            val o : DBObject = data
//            val reVal = db.queryObject(o, "providers").map (x => x).getOrElse(Map.empty)
//            (Some(Map("provider" -> toJson(reVal))), None)

            db.queryObject(o, "providers").map { reVal =>
                (Some(Map("provider" -> toJson(reVal)) ++ m), None)
            }.getOrElse(
                (Some(Map("provider" -> toJson("not exist")) ++ m), None)
            )

        } catch {
            case ex : Exception => println(s"push.error=${ex.getMessage}");(None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }

    def queryProviderOne(data : JsValue)
                        (pr : Option[Map[String, JsValue]])
                        (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {

        try {
            val conn = cm.modules.get.get("db").map(x => x.asInstanceOf[dbInstanceManager]).getOrElse(throw new Exception("no db connection"))
            val db = conn.queryDBInstance("cli").get

            val js = MergeStepResult(data, pr)
            val m = pr.map (x => x).getOrElse(Map.empty)

            import inner_traits.sc
            import inner_traits.d2m
            val o : DBObject = js
            db.queryObject(o, "providers").map { reVal =>
                (Some(Map("provider" -> toJson(reVal)) ++ m), None)
            }.getOrElse(
                (Some(Map("provider" -> toJson("not exist")) ++ m), None)
            )

        } catch {
            case ex : Exception => println(s"push.error=${ex.getMessage}");(None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }

    def queryProviderMulti(data : JsValue)
                          (pr : Option[Map[String, JsValue]])
                          (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {

        try {
            val conn = cm.modules.get.get("db").map(x => x.asInstanceOf[dbInstanceManager]).getOrElse(throw new Exception("no db connection"))
            val db = conn.queryDBInstance("cli").get

            val js = MergeStepResult(data, pr)
            val m = pr.map (x => x).getOrElse(Map.empty)

            import inner_traits.mc
            import inner_traits.d2m
            val o : DBObject = js
            val reVal = db.queryMultipleObject(o, "providers")

            (Some(m ++ Map("providers" -> toJson(reVal))), None)

        } catch {
            case ex : Exception => println(s"push.error=${ex.getMessage}");(None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }

    def searchProviders(data : JsValue)
                       (pr : Option[Map[String, JsValue]])
                       (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {

        try {
            val conn = cm.modules.get.get("db").map(x => x.asInstanceOf[dbInstanceManager]).getOrElse(throw new Exception("no db connection"))
            val db = conn.queryDBInstance("cli").get

            val js = MergeStepResult(data, pr)
            val m = pr.map (x => x).getOrElse(Map.empty)

            import inner_traits.asc
            import inner_traits.d2m
            val o : DBObject = js
            val reVal = db.queryMultipleObject(o, "providers")

            (Some(m ++ Map("providers" -> toJson(reVal))), None)

        } catch {
            case ex : Exception => println(s"search provider.error=${ex.getMessage}");(None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }

    def mergeCheckedProvider(data : JsValue)
                            (pr : Option[Map[String, JsValue]])
                            (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {

        try {
            val js = MergeStepResult(data, pr)
            val m = pr.map (x => x).getOrElse(Map.empty)

            val providers = (js \ "providers").asOpt[List[JsValue]].get
            val checked_lst = (js \ "checked_lst").asOpt[List[String]].get
            val providers_up =
                providers.map { iter =>
                    val pid = (iter \ "provider_id").asOpt[String].get
                    val is_checked = toJson(if (checked_lst.contains(pid)) 1 else 0)
                    toJson(iter.as[JsObject].value.toMap ++ Map("is_checked" -> is_checked))
                }

            (Some(m ++ Map("providers" -> toJson(providers_up))), None)

        } catch {
            case ex : Exception => println(s"search provider.error=${ex.getMessage}");(None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }

    def mergeCheckedProviderOne(data : JsValue)
                               (pr : Option[Map[String, JsValue]])
                               (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {

        try {
            val js = MergeStepResult(data, pr)
            val m = pr.map (x => x).getOrElse(Map.empty)

            val provider = (js \ "provider").asOpt[JsValue].get
            val checked_lst = (js \ "checked_lst").asOpt[List[String]].get

            val pid = (provider \ "provider_id").asOpt[String].get
            val is_checked = toJson(if (checked_lst.contains(pid)) 1 else 0)
            val provider_another = toJson(provider.as[JsObject].value.toMap ++ Map("is_checked" -> is_checked))

            (Some(m ++ Map("provider" -> toJson(provider_another))), None)

        } catch {
            case ex : Exception => println(s"search provider.error=${ex.getMessage}");(None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }

    def dropUnwantedMessage(data : JsValue)
                           (pr : Option[Map[String, JsValue]])
                           (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {

        try {
            val m = pr.map (x => x).getOrElse(Map.empty)

            val tmp = m - "user" - "status" - "checked_lst" - "condition"
            (Some(tmp), None)

        } catch {
            case ex : Exception => println(s"drop Unwanted Message .error=${ex.getMessage}");(None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }

    def mergeTopProviders(data :JsValue)
                               (pr : Option[Map[String, JsValue]])
                               (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {

        try {
            val js = MergeStepResult(data, pr)
            val m = pr.map (x => x).getOrElse(Map.empty)

            val providers = (js \ "providers").asOpt[List[JsValue]].get
            val top_lst = (js \ "condition" \ "providers").asOpt[List[String]].get
            val providers_up =
                providers.map { iter =>
                    val pid = (iter \ "provider_id").asOpt[String].get
                    val is_top = toJson(if (top_lst.contains(pid)) 1 else 0)
                    toJson(iter.as[JsObject].value.toMap ++ Map("is_top" -> is_top))
                }

            (Some(m ++ Map("providers" -> toJson(providers_up))), None)

        } catch {
            case ex : Exception => println(s"search provider.error=${ex.getMessage}");(None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }

    def mergeTopProvidersOne(data :JsValue)
                               (pr : Option[Map[String, JsValue]])
                               (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {

        try {
            val js = MergeStepResult(data, pr)
            val m = pr.map (x => x).getOrElse(Map.empty)

            val provider = (js \ "provider").asOpt[JsValue].get
            val col_lst = (js \ "condition" \ "providers").asOpt[List[String]].get

            val pid = (provider \ "provider_id").asOpt[String].get
            val is_top = toJson(if (col_lst.contains(pid)) 1 else 0)
            val provider_another = toJson(provider.as[JsObject].value.toMap ++ Map("is_top" -> is_top))

            (Some(m ++ Map("provider" -> toJson(provider_another))), None)

        } catch {
            case ex : Exception => println(s"merge collected one.error=${ex.getMessage}");(None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }

    def resetProviderLogo(data : JsValue)
                         (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {

        try {
            val conn = cm.modules.get.get("db").map(x => x.asInstanceOf[dbInstanceManager]).getOrElse(throw new Exception("no db connection"))
            val db = conn.queryDBInstance("cli").get


            val count =
            {
                import inner_traits.d2m
                db.queryCount(DBObject(), "providers").get
            }

            db.queryMultipleObject(DBObject(), "providers", skip = 0, take = count) { iter =>
                val brand_name = iter.get("brand_name").asInstanceOf[String]
                val short_name = iter.get("short_name").asInstanceOf[String]
                println(brand_name + "-----------" + short_name)
                Map("a" -> toJson(short_name))
            }.map (x => x.get("a").get.asOpt[String].get).distinct.foreach { name =>
                val (_, uuid) = copyFiles(name, logo_path + name + ".jpg")
                db.queryMultipleObject(DBObject("short_name" -> name), "providers") { iter =>
                    if (iter.get("logo").asInstanceOf[String] == "") {
                        val tmp = iter.asInstanceOf[BasicDBObject]
                        tmp.append("logo", uuid)
                        println(tmp)
                        db.updateObject(tmp.asInstanceOf[DBObject], "providers", "_id")
                    }

                    Map("he" -> toJson("he"))
                }
            }

//            (Some(m ++ Map("providers" -> toJson(reVal))), None)
            (Some(Map("reset" -> toJson("success"))), None)

        } catch {
            case ex : Exception => println(s"search provider.error=${ex.getMessage}");(None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }

    def copyFiles(name : String, source : String) : (String, String) = {
        try {
            val image_uuid = UUID.randomUUID().toString
            val tar = new File(dst, image_uuid)
            val is = new FileInputStream(source)

            val os = new FileOutputStream(tar)
            val buf = new Array[Byte](1024)
            var len = is.read(buf)
            while (len != -1) {
                os.write(buf, 0, len)
                len = is.read(buf)
            }
            is.close()
            os.close()

            (name, image_uuid)
        } catch {
            case _ : Exception => (name, "")
        }
    }
}
