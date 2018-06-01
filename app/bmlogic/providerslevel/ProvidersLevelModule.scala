package bmlogic.providerslevel

import java.text.SimpleDateFormat
import java.util.{Calendar, Date, GregorianCalendar}

import bmlogic.common.mergestepresult.MergeStepResult
import bmlogic.providerslevel.ProvidersLevelData._
import bmlogic.providerslevel.ProvidersLevelMessage._
import com.mongodb.casbah.Imports._
import com.pharbers.ErrorCode
import com.pharbers.bmmessages.{CommonModules, MessageDefines}
import com.pharbers.bmpattern.ModuleTrait
import com.pharbers.dbManagerTrait.dbInstanceManager
import play.api.libs.json.{JsObject, JsValue}
import play.api.libs.json.Json.toJson

object ProvidersLevelModule extends ModuleTrait {

    def dispatchMsg(msg: MessageDefines)(pr: Option[Map[String, JsValue]])(implicit cm: CommonModules): (Option[Map[String, JsValue]], Option[JsValue]) = msg match {
        case msg_pushProvidersLevel(data) => pushProviderLevel(data)
        case msg_queryProvidersLevel(data) => queryProviderLevel(data)(pr)
        case msg_queryCollectedProviders(data) => queryCollectedProviders(data)(pr)
        case msg_queryTopProviders(data) => queryTopProviders(data)(pr)
        case msg_queryDisplayAges(data) => queryDisplayAges(data)(pr)
        case msg_mergeDisplayAges(data) => mergeDisplayAges(data)(pr)
        case msg_queryServiceDate(data) => queryServiceDate(data)(pr)
        case msg_mergeServiceDate(data) => mergeServiceDate(data)(pr)
        case msg_queryDisplayTimes(data) => queryDisplayTimes(data)(pr)
        case msg_mergeDisplayTimes(data) => mergeDisplayTimes(data)(pr)
        case msg_resetLevels(data) => resetLevels(data)
        case _ => ???
    }

    object inner_traits extends creation with condition with result

    def pushProviderLevel(data : JsValue)
                         (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {

        try {
            val conn = cm.modules.get.get("db").map(x => x.asInstanceOf[dbInstanceManager]).getOrElse(throw new Exception("no db connection"))
            val db = conn.queryDBInstance("cli").get

            import inner_traits.m2d
            val o : DBObject = data
            db.insertObject(o, "levels", "_id")
            val reVal = o.get("_id").asInstanceOf[ObjectId].toString

            (Some(Map("level_id" -> toJson(reVal))), None)
        } catch {
            case ex : Exception => println(s"push level.error=${ex.getMessage}");(None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }

    def queryProviderLevel(data : JsValue)
                          (pr : Option[Map[String, JsValue]])
                          (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {

        try {
            val conn = cm.modules.get.get("db").map(x => x.asInstanceOf[dbInstanceManager]).getOrElse(throw new Exception("no db connection"))
            val db = conn.queryDBInstance("cli").get

            val js = MergeStepResult(data, pr)
            val m = pr.map (x => x).getOrElse(Map.empty)

            import inner_traits.sc
            import inner_traits.sr
            val o : DBObject = js
            val reVal = db.queryObject(o, "levels")
                            .map (x => x.get("ll").get.asOpt[String].get).getOrElse("scores_A")

            (Some(m ++ Map("level" -> toJson(reVal))), None)

        } catch {
            case ex : Exception => println(s"query level.error=${ex.getMessage}");(None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }

    def queryCollectedProviders(data : JsValue)
                               (pr : Option[Map[String, JsValue]])
                               (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {

        try {
            val conn = cm.modules.get.get("db").map(x => x.asInstanceOf[dbInstanceManager]).getOrElse(throw new Exception("no db connection"))
            val db = conn.queryDBInstance("cli").get

            val js = MergeStepResult(data, pr)
            val m = pr.map (x => x).getOrElse(Map.empty)
            val condition = (js \ "condition").asOpt[JsValue].map (x => x).getOrElse(toJson(Map("wx" -> toJson("code"))))

            import inner_traits.spc
            import inner_traits.colr
            val o : DBObject = js
            val reVal = db.queryMultipleObject(o, "levels")
                            .map (x => x.get("provider_id").get.asOpt[String].get)

            val tmp = toJson(condition.as[JsObject].value.toMap ++ Map("providers" -> toJson(reVal)))
            (Some(m ++ Map("condition" -> tmp)), None)

        } catch {
            case ex : Exception => println(s"query collect.error=${ex.getMessage}");(None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }

    def queryTopProviders(data : JsValue)
                               (pr : Option[Map[String, JsValue]])
                               (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {

        try {
            val conn = cm.modules.get.get("db").map(x => x.asInstanceOf[dbInstanceManager]).getOrElse(throw new Exception("no db connection"))
            val db = conn.queryDBInstance("cli").get

            val js = MergeStepResult(data, pr)
            val m = pr.map (x => x).getOrElse(Map.empty)
            val condition = (js \ "condition").asOpt[JsValue].map (x => x).getOrElse(toJson(Map("wx" -> toJson("code"))))

            import inner_traits.tpc
            import inner_traits.colr
            val o : DBObject = js
            val reVal = db.queryMultipleObject(o, "levels")
                .map (x => x.get("provider_id").get.asOpt[String].get)

            val tmp = toJson(condition.as[JsObject].value.toMap ++ Map("providers" -> toJson(reVal)))
            (Some(m ++ Map("condition" -> tmp)), None)

        } catch {
            case ex : Exception => println(s"query top.error=${ex.getMessage}");(None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }

    def queryDisplayAges(data : JsValue)
                        (pr : Option[Map[String, JsValue]])
                        (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {

        try {
            val conn = cm.modules.get.get("db").map(x => x.asInstanceOf[dbInstanceManager]).getOrElse(throw new Exception("no db connection"))
            val db = conn.queryDBInstance("cli").get

            val js = MergeStepResult(data, pr)
            val m = pr.map (x => x).getOrElse(Map.empty)
//            val condition = (js \ "condition").asOpt[JsValue].map (x => x).getOrElse(toJson(Map("wx" -> toJson("code"))))

            import inner_traits.msc
            import inner_traits.colr
            val o : DBObject = js
            val reVal = db.queryMultipleObject(o, "levels", take = 300)

            (Some(m ++ Map("providers_ages" -> toJson(reVal))), None)

        } catch {
            case ex : Exception => println(s"query display age.error=${ex.getMessage}");(None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }

    def mergeDisplayAges(data : JsValue)
                        (pr : Option[Map[String, JsValue]])
                        (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {

        try {

            val js = MergeStepResult(data, pr)
            val m = pr.map (x => x).getOrElse(Map.empty)

//            val provider_ages = (js \ "providers_ages").asOpt[List[JsValue]].get
            val providers = (js \ "providers").asOpt[List[JsValue]].get

            val provider_new =
                providers.map { iter =>
                    val ages = (iter \ "age").asOpt[List[String]].get
                    var str = ""
                    ages.foreach { iter =>
                        str += iter + "岁 "
                    }
                    val m_iter = iter.as[JsObject].value.toMap
                    toJson(m_iter ++ Map("age" -> toJson(str.trim)))
                }

            val tmp = m - "provider_ages"
            (Some(tmp ++ Map("providers" -> toJson(provider_new))), None)

        } catch {
            case ex : Exception => println(s"merge display age.error=${ex.getMessage}");(None, Some(ErrorCode.errorToJson(ex.getMessage)))
//            case ex : Exception => ex.printStackTrace();(None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }

    def queryServiceDate(data : JsValue)
                        (pr : Option[Map[String, JsValue]])
                        (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {

        try {
            val conn = cm.modules.get.get("db").map(x => x.asInstanceOf[dbInstanceManager]).getOrElse(throw new Exception("no db connection"))
            val db = conn.queryDBInstance("cli").get

            val js = MergeStepResult(data, pr)
            val m = pr.map (x => x).getOrElse(Map.empty)
            // val condition = (js \ "condition").asOpt[JsValue].map (x => x).getOrElse(toJson(Map("wx" -> toJson("code"))))

            import inner_traits.soc
            import inner_traits.tr
            val o : DBObject = js
            val reVal = db.queryMultipleObject(o, "levels").map { iter =>
                val ssd : Calendar = new GregorianCalendar()
                ssd.setTime(new Date(iter.get("ssd").get.asOpt[Long].get))

                val sed : Calendar = new GregorianCalendar()
                sed.setTime(new Date(iter.get("sed").get.asOpt[Long].get))

                val age = iter.get("age").get.asOpt[String].get

                val df = new SimpleDateFormat("MMdd")
                val df2 = new SimpleDateFormat("HH:mm")
                var lst : List[Map[String, JsValue]]= Nil
                while (ssd.getTimeInMillis <= sed.getTimeInMillis) {
                    val sd = df.format(ssd.getTime)
                    val sh = df2.format(ssd.getTime)
                    val eh = df2.format(sed.getTime)
                    lst =
                        Map (
                            "day" -> toJson(sd),
                            "hourstart" -> toJson(sh),
                            "hourend" -> toJson(eh),
                            "age" -> toJson(age)
                        ) :: lst
                    ssd.add(Calendar.DAY_OF_MONTH, 1)
                }

//                (ssd_month, ssd_day) :: (sed_month, sed_day) :: Nil
                lst

            }.flatten.groupBy(_.get("day").get.asOpt[String].get).map { iter =>
                Map (
                    "day" -> toJson(iter._1),
                    "detail" -> toJson(iter._2.sortBy(p => p.get("hourstart").get.asOpt[String].get))
                )
            }.toList.sortBy(p => p.get("day").get.asOpt[String].get)

            (Some(m ++ Map("server_time" -> toJson(reVal))), None)

        } catch {
            case ex : Exception => println(s"query service date.error=${ex.getMessage}");(None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }

    def mergeServiceDate(data : JsValue)
                        (pr : Option[Map[String, JsValue]])
                        (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {

        try {

            val js = MergeStepResult(data, pr)
            val m = pr.map (x => x).getOrElse(Map.empty)

            val server_time = (js \ "server_time").asOpt[JsValue].get
            val provider = (js \ "provider").asOpt[JsValue].get

            val pm = provider.as[JsObject].value.toMap

            val tmp = m - "server_time"
            (Some(tmp ++ Map("provider" -> toJson(pm ++ Map("server_time" -> server_time)))), None)

        } catch {
            case ex : Exception => println(s"merge display age.error=${ex.getMessage}");(None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }

    def resetLevels(data : JsValue)
                   (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {
        try {
            val conn = cm.modules.get.get("db").map(x => x.asInstanceOf[dbInstanceManager]).getOrElse(throw new Exception("no db connection"))
            val db = conn.queryDBInstance("cli").get

            val count =
            {
                import inner_traits.tr
                db.queryCount(DBObject(), "levels").get
            }
            db.queryMultipleObject(DBObject(), "levels", skip = 0, take = count) { iter =>
                val provider_id = iter.getAs[String]("provider_id").get
                val tmp =
                    db.queryObject(DBObject("_id" -> new ObjectId(provider_id)), "providers") { iter =>
                        Map("provider_id" -> toJson(iter.getAs[ObjectId]("_id").get.toString))
                    }
                tmp match {
                    case None => Map("_id" -> toJson(iter.getAs[ObjectId]("_id").get.toString))
                    case Some(x) => println(x); Map("_id" -> toJson(""))
                }
            }.map (x => x.get("_id").get.asOpt[String].get).distinct.filterNot(_ == "").foreach { unwanted =>
                db.deleteObject(DBObject("_id" -> new ObjectId(unwanted)), "levels", "_id")
            }

            (Some(Map("reset" -> toJson("success"))), None)

        } catch {
            case ex : Exception => println(s"reset levels.error=${ex.getMessage}");(None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }

    def queryDisplayTimes(data : JsValue)
                         (pr : Option[Map[String, JsValue]])
                         (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {

        try {
            val conn = cm.modules.get.get("db").map(x => x.asInstanceOf[dbInstanceManager]).getOrElse(throw new Exception("no db connection"))
            val db = conn.queryDBInstance("cli").get

            val js = MergeStepResult(data, pr)
            val m = pr.map (x => x).getOrElse(Map.empty)

            val count =
            {
                import inner_traits.tr
                db.queryCount(DBObject(), "levels").get
            }

            import inner_traits.ttc
            val reVal =
                db.queryMultipleObject(data, "levels", skip = 0, take = count) { iter =>
                    val provider_id = iter.getAs[String]("provider_id").get
                    Map (
                        "provider_id" -> toJson(provider_id)
                    )
                }.map (x => x.get("provider_id").get.asOpt[String].get)

            (Some(m ++ Map("time_over" -> toJson(reVal))), None)

        } catch {
            case ex : Exception => println(s"query display time.error=${ex.getMessage}");(None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }

    }

    def mergeDisplayTimes(data : JsValue)
                         (pr : Option[Map[String, JsValue]])
                         (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {
        try {

            val js = MergeStepResult(data, pr)
            val m = pr.map (x => x).getOrElse(Map.empty)

            val time_over = (js \ "time_over").asOpt[List[String]].get
            val providers = (js \ "providers").asOpt[List[JsValue]].get

            val providers_up =
                providers.map { iter =>
                    val pid = (iter \ "provider_id").asOpt[String].get
                    if (time_over.contains(pid)) Some(iter)
                    else (None)
                }.filterNot(_ == None).map (_.get)

            val tmp = m - "time_over"
            (Some(tmp ++ Map("providers" -> toJson(providers_up))), None)

        } catch {
            case ex : Exception => println(s"merge display time.error=${ex.getMessage}");(None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }
}
