package kr.hs.dgsw.trust.server.data.entity

import org.springframework.boot.configurationprocessor.json.JSONArray
import org.springframework.boot.configurationprocessor.json.JSONObject
import java.sql.Timestamp
import javax.persistence.*

@Entity(name = "post")
class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Int? = null
    var username: String? = null
    @Column(name = "created_at")
    var createdAt: Timestamp? = null
    var content: String? = null
    var imageList: String? = null
    var isAnonymous: Boolean? = null
}

fun Post.toJsonObject(): JSONObject {
    val jsonObject = JSONObject()
    val jsonArray = JSONArray(imageList)
    jsonObject.put("id", id)
    jsonObject.put("createdAt", createdAt)
    jsonObject.put("content", content)
    jsonObject.put("imageList", jsonArray)
    jsonObject.put("isAnonymous", isAnonymous)
    return jsonObject
}