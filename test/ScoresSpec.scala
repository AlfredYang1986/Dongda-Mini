import org.specs2.mutable.Specification
import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson
import play.api.test.WsTestClient

import scala.concurrent.Await
import scala.concurrent.duration._

class ScoresSpec extends Specification {
    //    with BeforeAll with AfterAll {

    import scala.concurrent.ExecutionContext.Implicits.global

    val time_out = 2 second

    lazy val checkin_info = toJson(
        Map(
            "condition" -> toJson(Map(
                "brand_name" -> toJson("我是一个联系人"),
                "wechat_id" -> toJson("oV3gY406OgRJgmT0pOtC3DOr4w0M")
            ))
        )
    )

    lazy val checkin_info_2 = toJson(
        Map(
            "condition" -> toJson(Map(
                "brand_name" -> toJson("我不是一个品牌"),
                "wechat_id" -> toJson("oV3gY406OgRJgmT0pOtC3DOr4w0M")
            ))
        )
    )

    override def is = s2"""
        This is a dongda to check the profile logic string

            The 'dongda' check in
                check in with scrores           $checkinWithScoresTest
                                                                              """


    def checkinWithScoresTest = {
        WsTestClient.withClient { client =>
            val reVal = Await.result(
                new DongdaClient(client, "http://127.0.0.1:9000").checkinWithScores(checkin_info), time_out)

            val result = (reVal \ "result").asOpt[JsValue].get

            println(result)
            (result \ "scores" \ "scores_A").asOpt[Int].get must_== 0
            (result \ "scores" \ "scores_B").asOpt[Int].get must_== 1
            (result \ "scores" \ "scores_C").asOpt[Int].get must_== 0
        }

        WsTestClient.withClient { client =>
            val reVal = Await.result(
                new DongdaClient(client, "http://127.0.0.1:9000").checkinWithScores(checkin_info), time_out)

            val result = (reVal \ "result").asOpt[JsValue].get

            println(result)
            (result \ "scores" \ "scores_A").asOpt[Int].get must_== 0
            (result \ "scores" \ "scores_B").asOpt[Int].get must_== 1
            (result \ "scores" \ "scores_C").asOpt[Int].get must_== 0
        }

        WsTestClient.withClient { client =>
            val reVal = Await.result(
                new DongdaClient(client, "http://127.0.0.1:9000").checkinWithScores(checkin_info_2), time_out)

            val result = (reVal \ "result").asOpt[JsValue].get

            println(result)
            (result \ "scores" \ "scores_A").asOpt[Int].get must_== 1
            (result \ "scores" \ "scores_B").asOpt[Int].get must_== 1
            (result \ "scores" \ "scores_C").asOpt[Int].get must_== 0
        }
    }
}