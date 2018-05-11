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
}
