//import java.util.Date
//
//import org.specs2.mutable.Specification
//import play.api.libs.json.JsValue
//import play.api.libs.json.Json.toJson
//import play.api.test.WsTestClient
//
//import scala.concurrent.Await
//import scala.concurrent.duration._
//
//class LevelSpec extends Specification {
////    with BeforeAll with AfterAll {
//
//    import scala.concurrent.ExecutionContext.Implicits.global
//
//    val time_out = 2 second
//
//    lazy val level_info = toJson(
//        Map(
//            "level" -> toJson(Map(
//                "provider_id" -> toJson("5af54e1410f5c90836f1e682"),
//                "ll" -> toJson("scores_D"),
//                "is_collected" -> toJson(1),
//                "sd" -> toJson(1526227200000L),
//                "ed" -> toJson(1526572800000L)
//            ))
//        )
//    )
//
//    override def is = s2"""
//        This is a dongda to check the profile logic string
//
//            The 'dongda' push level
//                push level                          $pushLevelTest
//                                                                              """
////    query level                          $queryLevelTest
//
//    def pushLevelTest = {
//        WsTestClient.withClient { client =>
//            val reVal = Await.result(
//                new DongdaClient(client, "http://127.0.0.1:9999").pushLevel(level_info), time_out)
//
//            val result = (reVal \ "result").asOpt[JsValue].get
//
//            println(result)
//            (result \ "level_id").asOpt[String].get.length must_!= 0
//        }
//    }
//
//    def queryLevelTest = {
//        WsTestClient.withClient { client =>
//            val reVal = Await.result(
//                new DongdaClient(client, "http://127.0.0.1:9000").queryLevel("5af54e1410f5c90836f1e682"), time_out)
//
//            val result = (reVal \ "result").asOpt[JsValue].get
//
//            println(result)
//            (result \ "level").asOpt[String].get must_== "scores_B"
//        }
//
//        WsTestClient.withClient { client =>
//            val reVal = Await.result(
//                new DongdaClient(client, "http://127.0.0.1:9000").queryLevel("123456789"), time_out)
//
//            val result = (reVal \ "result").asOpt[JsValue].get
//
//            println(result)
//            (result \ "level").asOpt[String].get must_== "scores_A"
//        }
//    }
//}
