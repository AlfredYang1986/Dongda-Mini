import java.util.Date

import javax.inject.Inject
import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson
import play.api.libs.ws.WSClient
import play.api.test.WsTestClient

import scala.concurrent.{Await, ExecutionContext, Future}

/**
  * Created by alfredyang on 07/07/2017.
  */
class DongdaClient(ws: WSClient, baseUrl: String)(implicit ec: ExecutionContext) {
    @Inject def this(ws: WSClient, ec: ExecutionContext) = this(ws, "http://127.0.0.1:9000")(ec)

    def pushProviderApply(push_info : JsValue) ={
        ws.url(baseUrl + "/provider/apply/push")
            .withHeaders("Accept" -> "application/json", "Content-Type" -> "application/json")
            .post(push_info)
            .map { response =>
                // println(response.json)
                response.json
            }
    }

    def popProviderApply(apply_id : String) = {
        ws.url(baseUrl + "/provider/apply/pop")
            .withHeaders("Accept" -> "application/json", "Content-Type" -> "application/json")
            .post(toJson(Map(
                "condition" -> toJson(Map(
                    "apply_id" -> toJson(apply_id)
                ))
            )))
            .map { response =>
                // println(response.json)
                response.json
            }
    }

    def queryProviderApply(apply_id : String) = {
        ws.url(baseUrl + "/provider/apply/query")
            .withHeaders("Accept" -> "application/json", "Content-Type" -> "application/json")
            .post(toJson(Map(
                "condition" -> toJson(Map(
                    "apply_id" -> toJson(apply_id)
                ))
            )))
            .map { response =>
                // println(response.json)
                response.json
            }
    }

    def searchProviderApply(wechat_id : String, approved : Int = 0) = {
        ws.url(baseUrl + "/provider/apply/search")
            .withHeaders("Accept" -> "application/json", "Content-Type" -> "application/json")
            .post(toJson(Map(
                "condition" -> toJson(Map(
                    "wechat_open_id" -> toJson(wechat_id),
                    "approved" -> toJson(approved)
                ))
            )))
            .map { response =>
                // println(response.json)
                response.json
            }
    }

    def pushProvider(push_info : JsValue) ={
        ws.url(baseUrl + "/provider/push")
            .withHeaders("Accept" -> "application/json", "Content-Type" -> "application/json")
            .post(push_info)
            .map { response =>
                // println(response.json)
                response.json
            }
    }

    def searchProviders(wechat_id : String) ={
        ws.url(baseUrl + "/provider/search")
            .withHeaders("Accept" -> "application/json", "Content-Type" -> "application/json")
            .post(toJson(Map(
                "condition" -> toJson(Map(
                    "wechat_id" -> toJson(wechat_id)
                ))
            )))
            .map { response =>
                // println(response.json)
                response.json
            }
    }

    def queryProvider(wechat_id : String, provider_id : String) = {
        ws.url(baseUrl + "/provider/query")
            .withHeaders("Accept" -> "application/json", "Content-Type" -> "application/json")
            .post(toJson(Map(
                "condition" -> toJson(Map(
                    "wechat_id" -> toJson(wechat_id),
                    "provider_id" -> toJson(provider_id)
                ))
            )))
            .map { response =>
                // println(response.json)
                response.json
            }
    }

    def queryCollectedProvider(wechat_id : String) = {
        ws.url(baseUrl + "/provider/collection")
            .withHeaders("Accept" -> "application/json", "Content-Type" -> "application/json")
            .post(toJson(Map(
                "condition" -> toJson(Map(
                    "wechat_id" -> toJson(wechat_id)
                ))
            )))
            .map { response =>
                // println(response.json)
                response.json
            }
    }

    def checkinWithScores(check_info : JsValue) = {
        ws.url(baseUrl + "/checkin/scores")
            .withHeaders("Accept" -> "application/json", "Content-Type" -> "application/json")
            .post(check_info)
            .map { response =>
                // println(response.json)
                response.json
            }
    }

    def queryScores(wechat_id : String) = {
        ws.url(baseUrl + "/scores/query")
            .withHeaders("Accept" -> "application/json", "Content-Type" -> "application/json")
            .post(toJson(Map(
                "condition" -> toJson(Map(
                    "wechat_id" -> wechat_id
                ))
            )))
            .map { response =>
                // println(response.json)
                response.json
            }
    }

    def pushLevel(level_info : JsValue) = {
        ws.url(baseUrl + "/level/push")
            .withHeaders("Accept" -> "application/json", "Content-Type" -> "application/json")
            .post(level_info)
            .map { response =>
                // println(response.json)
                response.json
            }
    }

    def queryLevel(provider_id : String) = {
        ws.url(baseUrl + "/level/query")
            .withHeaders("Accept" -> "application/json", "Content-Type" -> "application/json")
            .post(toJson(Map(
                "condition" -> toJson(Map(
                    "provider_id" -> toJson(provider_id)
                ))
            )))
            .map { response =>
                // println(response.json)
                response.json
            }
    }

    def pushAnswers(push_info : JsValue) = {
        ws.url(baseUrl + "/answer/push")
            .withHeaders("Accept" -> "application/json", "Content-Type" -> "application/json")
            .post(push_info)
            .map { response =>
                // println(response.json)
                response.json
            }
    }

    def randomAnswers(wechat_id : String) = {
        ws.url(baseUrl + "/answer/random")
            .withHeaders("Accept" -> "application/json", "Content-Type" -> "application/json")
            .post(toJson(Map(
                "condition" -> toJson(Map(
                    "wechat_id" -> wechat_id
                ))
            )))
            .map { response =>
                // println(response.json)
                response.json
            }
    }

    def checkAnswers(wechat_id : String, check_lst : List[String], answer_lst : List[Int]) = {
        val ua =
            (check_lst zip answer_lst) map { x =>
                toJson(Map(
                    "answer_id" -> toJson(x._1),
                    "answer" -> toJson(x._2)
                ))
            }

        ws.url(baseUrl + "/answer/check")
            .withHeaders("Accept" -> "application/json", "Content-Type" -> "application/json")
            .post(toJson(Map(
                "condition" -> toJson(Map(
                    "wechat_id" -> toJson(wechat_id),
                    "answers" -> toJson(check_lst)

                )),
                "answers" -> toJson(ua)
            )))
            .map { response =>
                // println(response.json)
                response.json
            }
    }
}
