package kr.hs.dgsw.trust.server.data.dto

import org.springframework.boot.configurationprocessor.json.JSONObject

class AccountDTO(
    val name: String,
    val username: String,
    val profileImage: String?,
)

fun AccountDTO.toJsonObject(): JSONObject {
    val jsonObject = JSONObject()
    jsonObject.put("name", name)
    jsonObject.put("username", username)
    jsonObject.put("profileImage", profileImage)
    return jsonObject
}