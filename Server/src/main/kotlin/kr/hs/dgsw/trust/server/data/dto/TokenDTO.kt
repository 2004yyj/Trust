package kr.hs.dgsw.trust.server.data.dto

import lombok.*
import org.springframework.boot.configurationprocessor.json.JSONObject

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
class TokenDTO(val token: String, val username: String)

fun TokenDTO.toJsonObject(): JSONObject {
    val jsonObject = JSONObject()
    jsonObject.put("token", token)
    jsonObject.put("username", username)
    return jsonObject
}