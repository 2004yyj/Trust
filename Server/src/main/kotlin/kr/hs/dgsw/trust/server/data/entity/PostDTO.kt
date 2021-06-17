package kr.hs.dgsw.trust.server.data.entity

import org.springframework.boot.configurationprocessor.json.JSONArray
import org.springframework.boot.configurationprocessor.json.JSONObject
import java.sql.Timestamp
import javax.persistence.*

class PostDTO(
    var id: Int,
    var username: String,
    var createdAt: Timestamp,
    var content: String,
    var imageList: String,
    var isAnonymous: Boolean
)

fun PostDTO.toJsonObject(): JSONObject {
    val jsonObject = JSONObject()
    val jsonArray = JSONArray(imageList)
    jsonObject.put("id", id)
    jsonObject.put("createdAt", createdAt?.time)
    jsonObject.put("content", content)
    jsonObject.put("imageList", jsonArray)
    jsonObject.put("isAnonymous", isAnonymous)
    return jsonObject
}