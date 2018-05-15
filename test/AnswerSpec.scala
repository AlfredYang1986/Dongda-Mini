import org.specs2.mutable.Specification
import org.specs2.specification.{AfterAll, BeforeAll}
import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson
import play.api.test.WsTestClient

import scala.concurrent.Await
import scala.concurrent.duration._

class AnswerSpec extends Specification {
    //    with BeforeAll with AfterAll {

    import scala.concurrent.ExecutionContext.Implicits.global

    val time_out = 2 second
    var provider_id : String = ""

    def answer_info(index : Int) = toJson(
        Map(
            "answer" -> toJson(Map(
                "description" -> toJson(s"我不是一个题目 $index"),
                "choice" -> toJson(Map(
                    "index" -> toJson(1),
                    "title" -> toJson("一个选择A"),
                    "image" -> toJson("")
                ) :: Map(
                    "index" -> toJson(2),
                    "title" -> toJson("一个选择B"),
                    "image" -> toJson("")
                ) :: Map(
                    "index" -> toJson(3),
                    "title" -> toJson("一个选择C"),
                    "image" -> toJson("")
                ) :: Map(
                    "index" -> toJson(4),
                    "title" -> toJson("一个选择D"),
                    "image" -> toJson("")
                ) :: Nil),
                "random" -> toJson(index),
                "answer" -> toJson(1)
            ))
        )
    )

    //    override def beforeAll(): Unit = pushProviderApplyTest
    //    override def afterAll() : Unit = popProviderApplyTest

    override def is = s2"""
        This is a dongda to check the profile logic string

            The 'dongda' answers test
                random 5 answer info              $randomAnswersTest
                                                                              """
//    push 5 answer info              $pushMultiAnswer

    def pushMultiAnswer = {
        0 until 9 map { x =>
            WsTestClient.withClient { client =>
                val reVal = Await.result(
                    new DongdaClient(client, "http://127.0.0.1:9000").pushAnswers(answer_info(x)), time_out)

                println(reVal)
                val result = (reVal \ "result").asOpt[JsValue].get
                val answer_id = (result \ "answer" \ "answer_id").asOpt[String].get
                answer_id.length must_!= 0
            }
        }
    }

    def randomAnswersTest = {
//          oV3gY45JriaqY8EiXruXSHtt4xj0
//          oV3gY406OgRJgmT0pOtC3DOr4w0M
        WsTestClient.withClient { client =>
            val reVal = Await.result(
                new DongdaClient(client, "http://127.0.0.1:9000").randomAnswers("oV3gY45JriaqY8EiXruXSHtt4xj0"), time_out)

            println(reVal)
            val result = (reVal \ "result").asOpt[JsValue].get
            val answers = (result \ "answers").asOpt[List[JsValue]].get
            answers.length must_!= 0
        }
    }
}