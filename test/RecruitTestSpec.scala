//import org.specs2.mutable.Specification
//import org.specs2.specification.{AfterAll, BeforeAll}
//import play.api.libs.json.JsValue
//import play.api.libs.json.Json.toJson
//import play.api.test.WsTestClient
//
//import scala.concurrent.Await
//import scala.concurrent.duration._
//
//class RecruitTestSpec extends Specification
//    with BeforeAll with AfterAll {
//
//    import scala.concurrent.ExecutionContext.Implicits.global
//
//    val time_out = 2 second
//    var apply_id : String = ""
//
//    lazy val apply_push_info = toJson(
//        Map(
//            "apply" -> toJson(Map(
//                "contact" -> toJson("我是一个联系人"),
//                "contact_no" -> toJson("我是联系人的电话"),
//                "brand_name" -> toJson("品牌名称"),
//                "service_type" -> toJson("我是service——type"),
//                "service_leaf" -> toJson("我是一个leaf"),
//                "input_pwd" -> toJson("我是一个Input"),
//                "wechat_user" -> toJson(Map(
//                    "wechat_name" -> toJson("微信名"),
//                    "wechat_photo" -> toJson("微信头像地址"),
//                    "wechat_open_id" -> toJson("微信id"),
//                    "wechat_token" -> toJson("微信token")
//                )),
//                "age_boundary" -> toJson(Map(
//                    "low" -> toJson(2),
//                    "up" -> toJson(8)
//                ))
//            ))
//        )
//    )
//
//    override def beforeAll(): Unit = pushProviderApplyTest
//    override def afterAll() : Unit = popProviderApplyTest
//
//    override def is = s2"""
//        This is a dongda to check the profile logic string
//
//            The 'dongda' provider push application
//                provider query                         $queryProviderApplyTest
//                provider search                        $searchProviderApplyTest
//                                                                              """
//
//    def pushProviderApplyTest = {
//        WsTestClient.withClient { client =>
//            val reVal = Await.result(
//                new DongdaClient(client, "http://127.0.0.1:9000").pushProviderApply(apply_push_info), time_out)
//
//            val result = (reVal \ "result").asOpt[JsValue].get
//            apply_id = (result \ "apply_id").asOpt[String].get
//
//            apply_id.length must_!= 0
//        }
//    }
//
//    def popProviderApplyTest = {
//        WsTestClient.withClient { client =>
//            val reVal = Await.result(
//                new DongdaClient(client, "http://127.0.0.1:9000").popProviderApply(apply_id), time_out)
//
//            val result = (reVal \ "result").asOpt[JsValue].get
//            (result \ "pop apply").asOpt[String].get must_== "success"
//        }
//    }
//
//    def queryProviderApplyTest = {
//        WsTestClient.withClient { client =>
//            val reVal = Await.result(
//                new DongdaClient(client, "http://127.0.0.1:9000").queryProviderApply(apply_id), time_out)
//
//            val result = (reVal \ "result").asOpt[JsValue].get
//            val apply = (result \ "apply").asOpt[JsValue].get
//
//            (apply \ "contact").asOpt[String].get must_== "我是一个联系人"
//        }
//    }
//
//    def searchProviderApplyTest = {
//        WsTestClient.withClient { client =>
//            val reVal = Await.result(
//                new DongdaClient(client, "http://127.0.0.1:9000").searchProviderApply("微信id"), time_out)
//
//            val result = (reVal \ "result").asOpt[JsValue].get
//            val apply = (result \ "applies").asOpt[List[JsValue]].get.head
//
//            (apply \ "contact").asOpt[String].get must_== "我是一个联系人"
//        }
//    }
//}