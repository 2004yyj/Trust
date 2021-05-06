package kr.hs.dgsw.trust.server.data.response

class JsonResponse {
    fun returnResponse(status: String, message: String, data: Any?) : HashMap<String, Any?> {
        val response = HashMap<String, Any?>()

        response["status"] = status
        response["message"] = message
        response["data"] = data

        return response
    }
}