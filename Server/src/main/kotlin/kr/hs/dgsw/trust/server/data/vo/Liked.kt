package kr.hs.dgsw.trust.server.data.vo

import org.springframework.boot.configurationprocessor.json.JSONObject
import java.sql.Timestamp
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity(name = "liked")
class Liked {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Int? = null
    var postId: Int? = null
    var username: String? = null
    var createdAt: Timestamp? = null
}

fun Liked.toJsonObject(): JSONObject {
    val jsonObject = JSONObject()
    jsonObject.put("id", id)
    jsonObject.put("postId", postId)
    jsonObject.put("username", username)
    jsonObject.put("createdAt", createdAt?.time)
    return jsonObject
}