package kr.hs.dgsw.trust.server.data.dto

import org.springframework.boot.configurationprocessor.json.JSONArray
import org.springframework.boot.configurationprocessor.json.JSONObject
import java.sql.Timestamp
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity(name = "comment")
class CommentVO {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Int? = null
    var postId: Int? = null
    var username: String? = null
    var createdAt: Timestamp? = null
    var content: String? = null
    var imageList : String? = null
}

fun CommentVO.toJsonObject(): JSONObject {
    val jsonObject = JSONObject()
    val jsonArray = JSONArray(imageList)
    jsonObject.put("id", id)
    jsonObject.put("postId", postId)
    jsonObject.put("createdAt", createdAt?.time)
    jsonObject.put("content", content)
    jsonObject.put("imageList", jsonArray)
    return jsonObject
}