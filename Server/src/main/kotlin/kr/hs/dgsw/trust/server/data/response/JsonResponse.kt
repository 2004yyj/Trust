package kr.hs.dgsw.trust.server.data.response

import org.springframework.boot.configurationprocessor.json.JSONObject

class JsonResponse<T>(
    private val status: String,
    private val message: String,
    private val data: T?
) {
    fun returnJsonObject() : String {
        val jsonObject = JSONObject()
        jsonObject.put("status", status)
        jsonObject.put("message", message)
        jsonObject.put("data", data)
        return jsonObject.toString()
    }
}