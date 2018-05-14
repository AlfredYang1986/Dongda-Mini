//import org.specs2.mutable.Specification
//import org.specs2.specification.{AfterAll, BeforeAll}
//import play.api.libs.json.JsValue
//import play.api.libs.json.Json.toJson
//import play.api.test.WsTestClient
//
//import scala.concurrent.Await
//import scala.concurrent.duration._
//
//class ProviderPushSpec extends Specification {
////    with BeforeAll with AfterAll {
//
//    import scala.concurrent.ExecutionContext.Implicits.global
//
//    val time_out = 2 second
//    var provider_id : String = ""
//
//    lazy val provider_info = toJson(
//        Map(
//            "provider" -> toJson(Map(
//                "brand_name" -> toJson("我不是一个品牌"),
//                "short_name" -> toJson("我是联系人的电话"),
//                "address" -> toJson("品牌名称"),
//                "service_type" -> toJson("我是service——type"),
//                "service_leaf" -> toJson("我是一个leaf"),
//                "logo" -> toJson("我是一个Input"),
//                "found_date" -> toJson("我是一个found_data"),
//                "onepunchline" -> toJson("我是一个onepunchline"),
//                "difference" -> toJson("我是一个different"),
//                "story" -> toJson("我是一个story"),
//                "description" -> toJson("description")
//            ))
//        )
//    )
//
////    override def beforeAll(): Unit = pushProviderApplyTest
////    override def afterAll() : Unit = popProviderApplyTest
//
//    override def is = s2"""
//        This is a dongda to check the profile logic string
//
//            The 'dongda' provider push application
//                provider push                         $pushProviderTest
//                                                                              """
//
//    def pushProviderTest = {
//        WsTestClient.withClient { client =>
//            val reVal = Await.result(
//                new DongdaClient(client, "http://127.0.0.1:9000").pushProvider(provider_info), time_out)
//
//            val result = (reVal \ "result").asOpt[JsValue].get
//            provider_id = (result \ "provider_id").asOpt[String].get
//
//            provider_id.length must_!= 0
//        }
//    }
//}