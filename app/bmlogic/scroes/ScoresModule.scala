package bmlogic.scroes

import bmlogic.common.mergestepresult.MergeStepResult
import bmlogic.scroes.ScoresData._
import bmlogic.scroes.ScoresMessage._
import com.mongodb.casbah.Imports._
import com.pharbers.ErrorCode
import com.pharbers.bmmessages.{CommonModules, MessageDefines}
import com.pharbers.bmpattern.ModuleTrait
import com.pharbers.dbManagerTrait.dbInstanceManager
import play.api.libs.json.{JsObject, JsValue}
import play.api.libs.json.Json.toJson

import scala.util.Random

object ScoresModule extends ModuleTrait {

    def dispatchMsg(msg: MessageDefines)(pr: Option[Map[String, JsValue]])(implicit cm: CommonModules): (Option[Map[String, JsValue]], Option[JsValue]) = msg match {
        case msg_addScores(data) => addScores(data)(pr)
        case msg_queryScores(data) => queryScores(data)(pr)
        case msg_preAnswerScores(data) => preAnswerScores(data)(pr)
        case msg_postAnswerScores(data) => postAnswerScores(data)(pr)
        case msg_postCheckInScores(data) => postCheckInScores(data)(pr)
    }

    object inner_traits extends creation with condition with result

    def addScores(data : JsValue)
                  (pr : Option[Map[String, JsValue]])
                  (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {

        try {
            val conn = cm.modules.get.get("db").map(x => x.asInstanceOf[dbInstanceManager]).getOrElse(throw new Exception("no db connection"))
            val db = conn.queryDBInstance("cli").get

            val js = MergeStepResult(data, pr)
            val m = pr.map (x => x).getOrElse(Map.empty)

            if ((js \ "check_in").asOpt[String].map (x => x == "already checked").getOrElse(false)) {
                (pr, None)
            } else {
                if ((js \ "scores").asOpt[String].map(x => x == "not exist").getOrElse(false)) {

                    import inner_traits.m2d
                    import inner_traits.d2m
                    val o: DBObject = js
                    db.insertObject(o, "scores", "_id")
                    val reVal: Map[String, JsValue] = o

                    (Some(m ++ Map("scores" -> toJson(reVal))), None)

                } else {

                    import inner_traits.qc
                    val o: DBObject = js
                    val reVal =
                        db.queryObject(o, "scores") { obj =>
                            val up: DBObject = inner_traits.up2d(js, obj)
                            db.updateObject(up, "scores", "_id")
                            import inner_traits.d2m
                            up
                        }

                    (Some(m ++ Map("scores" -> toJson(reVal))), None)
                }
            }

        } catch {
            case ex : Exception => println(s"add scores.error=${ex.getMessage}");(None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }

    def queryScores(data : JsValue)
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
            db.queryObject(o, "scores").map { x =>
                (Some(Map("scores" -> toJson(x)) ++ m), None)
            }.getOrElse {
                (Some(Map("scores" -> toJson("not exist")) ++ m), None)
            }

        } catch {
            case ex : Exception => println(s"query scores.error=${ex.getMessage}");(None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }

    def preAnswerScores(data : JsValue)
                       (pr : Option[Map[String, JsValue]])
                       (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {

        try {
            val conn = cm.modules.get.get("db").map(x => x.asInstanceOf[dbInstanceManager]).getOrElse(throw new Exception("no db connection"))
            val db = conn.queryDBInstance("cli").get

            val js = MergeStepResult(data, pr)
            val m = pr.map (x => x).getOrElse(Map.empty)

            if ((js \ "scores").asOpt[String].map(x => x == "not exist").getOrElse(false)) {
                (Some(m ++ Map("answer opp" -> toJson(0))), None)
            } else {
                val scores_js = (js \ "scores").asOpt[JsValue].get
                val scores = scores_js.as[JsObject].value.toMap
                val scores_B = scores.get("scores_B").get.asOpt[Int].get - 1
                if (scores_B > -1) {

                    import inner_traits.qc
                    val o: DBObject = js
                    val reVal =
                        db.queryObject(o, "scores") { obj =>
                            val up: DBObject = inner_traits.mb2d(js, obj)
                            db.updateObject(up, "scores", "_id")
                            import inner_traits.d2m
                            up
                        }

                    (Some(m ++ Map("answer opp" -> toJson(1), "scores" -> toJson(reVal))), None)
                } else {
                    (Some(m ++ Map("answer opp" -> toJson(0))), None)
                }
            }

        } catch {
            case ex : Exception => println(s"pre answer scores.error=${ex.getMessage}");(None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }

    def postAnswerScores(data : JsValue)
                        (pr : Option[Map[String, JsValue]])
                        (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {

        try {
            val conn = cm.modules.get.get("db").map(x => x.asInstanceOf[dbInstanceManager]).getOrElse(throw new Exception("no db connection"))
            val db = conn.queryDBInstance("cli").get

            val js = MergeStepResult(data, pr)
            val m = pr.map (x => x).getOrElse(Map.empty)

            if ((js \ "answers_check").asOpt[Int].map(x => x == 1).getOrElse(false)) {

                import inner_traits.sc
                val o: DBObject = js
                    db.queryObject(o, "scores") { obj =>
                        val up: DBObject = inner_traits.ac2d(js, obj)
                        db.updateObject(up, "scores", "_id")
                        import inner_traits.d2m
                        up
                    }
            }

            (Some(m - "user" - "status"), None)

        } catch {
            case ex : Exception => println(s"pre answer scores.error=${ex.getMessage}");(None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }

    def postCheckInScores(data : JsValue)
                         (pr : Option[Map[String, JsValue]])
                         (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {

        try {
            val conn = cm.modules.get.get("db").map(x => x.asInstanceOf[dbInstanceManager]).getOrElse(throw new Exception("no db connection"))
            val db = conn.queryDBInstance("cli").get

            val js = MergeStepResult(data, pr)
            val m = pr.map (x => x).getOrElse(Map.empty)

            val hc =
                if ((js \ "check_in").asOpt[String].map (x => x == "already checked").getOrElse(false)) 0
                else {
                    val possibility =
                        (js \ "level").asOpt[String].map (x => x).getOrElse("scores_A") match {
                            case "scores_A" => 5
                            case "scores_D" => 8
                        }

                    def has_coins(p : Int) = if (Random.nextInt(10) < p) 1 else 0

                    val tmp = has_coins(possibility)
                    if (tmp == 1) {
                        import inner_traits.qc
                        val o: DBObject = js
                        val reVal =
                            db.queryObject(o, "scores") { obj =>
                                val up: DBObject = inner_traits.ab2d(js, obj)
//                                val up: DBObject = inner_traits.mb2d(js, obj)
                                db.updateObject(up, "scores", "_id")
                                import inner_traits.d2m
                                up
                            }
                    }

                    tmp
                }

            (Some(m ++ Map("has_coins" -> toJson(hc))), None)

        } catch {
            case ex : Exception => println(s"post check in scores.error=${ex.getMessage}");(None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }
}
