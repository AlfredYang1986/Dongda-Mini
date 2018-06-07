package bmlogic.user

import java.util.Date

import bmlogic.common.mergestepresult.MergeStepResult
import bmlogic.user.UserData.{condition, creation, result}
import bmlogic.user.UserMessage._
import com.mongodb.casbah.Imports._
import com.pharbers.ErrorCode
import com.pharbers.bmmessages.{CommonModules, MessageDefines}
import com.pharbers.bmpattern.ModuleTrait
import com.pharbers.dbManagerTrait.dbInstanceManager
import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson

import java.util.Calendar
import java.util.GregorianCalendar

object UserModule extends ModuleTrait {

    def dispatchMsg(msg: MessageDefines)(pr: Option[Map[String, JsValue]])(implicit cm: CommonModules): (Option[Map[String, JsValue]], Option[JsValue]) = msg match {
        case msg_pushUser(data) => pushUser(data)(pr)
        case msg_popUser(data) => popUser(data)
        case msg_queryUser(data) => queryUser(data)(pr)
        case msg_searchUsers(data) => searchUsers(data)

        case msg_lastLoginTime(data) => lastLoginTime(data)(pr)
        case msg_updateLoginTime(data) => updateLoginTime(data)(pr)
        case msg_unWantedLogin(data) => unWantedLogin(data)(pr)
        case _ => ???
    }

    object inner_traits extends creation with result with condition

    def pushUser(data : JsValue)
                     (pr : Option[Map[String, JsValue]])
                     (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {

        try {
            val conn = cm.modules.get.get("db").map(x => x.asInstanceOf[dbInstanceManager]).getOrElse(throw new Exception("no db connection"))
            val db = conn.queryDBInstance("cli").get

            val js = MergeStepResult(data, pr)
            val m = pr.map (x => x).getOrElse(Map.empty)
            if ((js \ "user").asOpt[String].map (x => x == "not exist").getOrElse(false)) {
                import inner_traits.m2d
                val o : DBObject = js
                db.insertObject(o, "users", "_id")
                val reVal = o.get("_id").asInstanceOf[ObjectId].toString
                (Some(m ++ Map("user" -> toJson(Map("user_id" -> reVal)), "first_login" -> toJson(1))), None)
            } else {
                val user = (js \ "user").asOpt[JsValue].get
                val reVal = (user \ "user_id").asOpt[String].get
                (Some(m ++ Map("user" -> toJson(Map("user_id" -> reVal)))), None)
            }

        } catch {
            case ex : Exception => println(s"push.error=${ex.getMessage}");(None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }

    def popUser(data : JsValue)
                    (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {

        try {
            val conn = cm.modules.get.get("db").map(x => x.asInstanceOf[dbInstanceManager]).getOrElse(throw new Exception("no db connection"))
            val db = conn.queryDBInstance("cli").get

            import inner_traits.qc
            val o : DBObject = data
            db.deleteObject(o, "users", "_id")

            (Some(Map("pop user apply" -> toJson("success"))), None)

        } catch {
            case ex : Exception => println(s"pop.error=${ex.getMessage}");(None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }

    def queryUser(data : JsValue)
                 (pr : Option[Map[String, JsValue]])
                 (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {
        try {

            val conn = cm.modules.get.get("db").map(x => x.asInstanceOf[dbInstanceManager]).getOrElse(throw new Exception("no db connection"))
            val db = conn.queryDBInstance("cli").get

            val js = MergeStepResult(data, pr)
            val m = pr.map (x => x).getOrElse(Map.empty)

            import inner_traits.qc
            import inner_traits.d2m
            val o: DBObject = js
            db.queryObject(o, "users").map { reVal =>
                (Some(m ++ Map("user" -> toJson(reVal))), None)
            }.getOrElse(
                (Some(m ++ Map("user" -> toJson("not exist"))), None)
            )

        } catch {
            case ex: Exception => println(s"query.error=${ex.getMessage}"); (None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }

    def searchUsers(data : JsValue)
                       (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {

        try {

            val conn = cm.modules.get.get("db").map(x => x.asInstanceOf[dbInstanceManager]).getOrElse(throw new Exception("no db connection"))
            val db = conn.queryDBInstance("cli").get

            val take = (data \ "take").asOpt[Int].map (x => x).getOrElse(20)
            val skip = (data \ "skip").asOpt[Int].map (x => x).getOrElse(0)

            import inner_traits.sc
            import inner_traits.d2m
            val o : DBObject = data
            val reVal = db.queryMultipleObject(o, "users", take = take, skip = skip)

            (Some(Map("user" -> toJson(reVal))), None)

        } catch {
            case ex : Exception => println(s"search.error=${ex.getMessage}");(None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }

    def lastLoginTime(data : JsValue)
                     (pr : Option[Map[String, JsValue]])
                     (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {

        try {
            val js = MergeStepResult(data, pr)
            val m = pr.map (x => x).getOrElse(Map.empty)

            val user = (js \ "user").asOpt[JsValue].get
            val last = (user \ "last").asOpt[Long].get

            def isLengthThanOneDay : Boolean = {
                val c = new GregorianCalendar
                c.setTime(new Date(last))
                val mc = c.get(Calendar.MONTH) + 1
                val dc = c.get(Calendar.DAY_OF_MONTH)

                val nc = new GregorianCalendar
                nc.setTime(new Date())
                val mn = nc.get(Calendar.MONTH) + 1
                val dn = nc.get(Calendar.DAY_OF_MONTH)

                !(mn - mc == 0 && dn - dc < 1)
            }

            val moreThanOneDay = if (isLengthThanOneDay) 1 else 0

            (Some(m ++ Map("more than one day" -> toJson(moreThanOneDay))), None)

        } catch {
            case ex : Exception => println(s"last login time.error=${ex.getMessage}");(None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }

    def updateLoginTime(data : JsValue)
                       (pr : Option[Map[String, JsValue]])
                       (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {

        try {
            val conn = cm.modules.get.get("db").map(x => x.asInstanceOf[dbInstanceManager]).getOrElse(throw new Exception("no db connection"))
            val db = conn.queryDBInstance("cli").get

            val js = MergeStepResult(data, pr)
            val m = pr.map (x => x).getOrElse(Map.empty)

            val mtd = (js \ "more than one day").asOpt[Int].get
//            val user_id = (js \ "user" \ "user_id").asOpt[String].get
            if (mtd == 1) {
                import inner_traits.qc
                val o: DBObject = js
                db.queryObject(o, "users") { obj =>
                    val mm = toJson(Map("last" -> toJson(new Date().getTime)))
                    val tmp = inner_traits.up2d(obj, mm)
                    db.updateObject(tmp, "users", "_id")
                    Map("a" -> toJson("b"))
                }
            }

            (pr, None)

        } catch {
            case ex : Exception => println(s"update login time.error=${ex.getMessage}");(None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }

    def unWantedLogin(data : JsValue)
                     (pr : Option[Map[String, JsValue]])
                     (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {

        try {
            val js = MergeStepResult(data, pr)
            val m = pr.map (x => x).getOrElse(Map.empty)

            (Some(m - "more than one day"), None)

        } catch {
            case ex : Exception => println(s"unwanted login.error=${ex.getMessage}");(None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }
}