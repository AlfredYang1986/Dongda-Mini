package bmlogic.answer

import bmlogic.answer.answerData._
import bmlogic.answer.answerMessage._
import bmlogic.common.mergestepresult.MergeStepResult
import com.mongodb.casbah.Imports._
import com.pharbers.ErrorCode
import com.pharbers.bmmessages.{CommonModules, MessageDefines}
import com.pharbers.bmpattern.ModuleTrait
import com.pharbers.dbManagerTrait.dbInstanceManager
import org.bson.types.ObjectId
import play.api.libs.json.{JsObject, JsValue}
import play.api.libs.json.Json.toJson

import scala.util.Random

object answerModule extends ModuleTrait {

    def dispatchMsg(msg: MessageDefines)(pr: Option[Map[String, JsValue]])(implicit cm: CommonModules): (Option[Map[String, JsValue]], Option[JsValue]) = msg match {
        case msg_pushAnswer(data) => pushAnswer(data)
        case msg_checkAnswers(data) => checkAnswers(data)(pr)
        case msg_randomAnswers(data) => randomAnswers(data)(pr)
        case msg_randomGenerator(data) => randomGenerator(data)(pr)
        case msg_resetRandomIndex(data) => resetRandomIndex(data)
        case _ => ???
    }

    object inner_traits extends creation with condition with result

    def pushAnswer(data : JsValue)
                  (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {

        try {
            val conn = cm.modules.get.get("db").map(x => x.asInstanceOf[dbInstanceManager]).getOrElse(throw new Exception("no db connection"))
            val db = conn.queryDBInstance("cli").get

            import inner_traits.m2d
            val o : DBObject = data
            db.insertObject(o, "answers", "_id")
            val reVal = o.get("_id").asInstanceOf[ObjectId].toString

            (Some(Map("answer" -> toJson(Map("answer_id" -> toJson(reVal))))), None)

        } catch {
            case ex : Exception => println(s"push answer.error=${ex.getMessage}");(None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }

    def checkAnswers(data : JsValue)
                    (pr : Option[Map[String, JsValue]])
                    (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {

        try {
            val conn = cm.modules.get.get("db").map(x => x.asInstanceOf[dbInstanceManager]).getOrElse(throw new Exception("no db connection"))
            val db = conn.queryDBInstance("cli").get

            val js = MergeStepResult(data, pr)
            val m = pr.map (x => x).getOrElse(Map.empty)

            import inner_traits.mqc
            import inner_traits.sr
            val o : DBObject = data
            val left = (data \ "answers").asOpt[List[JsValue]].get
                            .sortBy(x => (x \ "answer_id").asOpt[String].get)
                                .map(x => (x \ "answer").asOpt[Int].get)

            val right = db.queryMultipleObject(o, "answers")
                            .sortBy(x => x.get("answer_id").get.asOpt[String].get)
                                .map (x => x.get("answer").get.asOpt[Int].get)

            val result = left zip right map (x => if (x._1 == x._2) 1 else 0) sum
            val reVal = if (result == left.length) 1 else 0

            (Some(m ++ Map("answers_check" -> toJson(reVal))), None)

        } catch {
            case ex : Exception => println(s"push answer.error=${ex.getMessage}");(None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }

    def randomAnswers(data :JsValue)
                     (pr : Option[Map[String, JsValue]])
                     (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {
        try {
            val conn = cm.modules.get.get("db").map(x => x.asInstanceOf[dbInstanceManager]).getOrElse(throw new Exception("no db connection"))
            val db = conn.queryDBInstance("cli").get

            val js = MergeStepResult(data, pr)
            val m = pr.map (x => x).getOrElse(Map.empty)

            import inner_traits.rqc
            import inner_traits.dr
            val o : DBObject = js

            val reVal = db.queryMultipleObject(o, "answers")
            val tmp = m - "user" - "random" - "scores" - "status"
//            val tmp = m - "user" - "status"
            (Some(tmp ++ Map("answers" -> toJson(reVal))), None)

        } catch {
            case ex : Exception => println(s"random answer.error=${ex.getMessage}");(None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }

    def randomGenerator(data :JsValue)
                       (pr : Option[Map[String, JsValue]])
                       (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {

        try {
            val conn = cm.modules.get.get("db").map(x => x.asInstanceOf[dbInstanceManager]).getOrElse(throw new Exception("no db connection"))
            val db = conn.queryDBInstance("cli").get

            import inner_traits.sr
            val count = db.queryCount(DBObject(), "answers").get
            println(count)

            val js = MergeStepResult(data, pr)
            val m = pr.map (x => x).getOrElse(Map.empty)

            if ((js \ "answer opp").asOpt[Int].map(x => x == 1).getOrElse(false)) {
                val a = randomNew(5, count)
                (Some(m ++ Map("random" -> toJson(a))), None)
            } else {
                val a : List[Int] = Nil
                (Some(m ++ Map("random" -> toJson(a))), None)
            }

        } catch {
            case ex : Exception => println(s"random generator error=${ex.getMessage}");(None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }

    def randomNew(n : Int, base : Int) = {
        var resultList : List[Int] = Nil
        while (resultList.length < n) {
            val randomNum = Random.nextInt(base)
//            val randomNum = 1 //Random.nextInt(9)
            if(!resultList.exists (s => s == randomNum)){
                resultList = resultList ::: List(randomNum)
            }
        }
        resultList
    }

    def resetRandomIndex(data : JsValue)
                        (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {

        try {
            val conn = cm.modules.get.get("db").map(x => x.asInstanceOf[dbInstanceManager]).getOrElse(throw new Exception("no db connection"))
            val db = conn.queryDBInstance("cli").get

            import inner_traits.sr
            val count = db.queryCount(DBObject(), "answers").get
            println(count)

            var random_index = 0
            val tmp =
                db.queryMultipleObject(DBObject(), "answers", skip = 0, take = count) { iter =>
                    iter += "random" -> random_index.asInstanceOf[Number]
                    random_index = random_index + 1
                    db.updateObject(iter, "answers", "_id")

                    Map("reset" -> toJson("random"))
                }

            (Some(Map("count" -> toJson(count))), None)
        } catch {
            case ex : Exception => println(s"reset random index error=${ex.getMessage}");(None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }
}
