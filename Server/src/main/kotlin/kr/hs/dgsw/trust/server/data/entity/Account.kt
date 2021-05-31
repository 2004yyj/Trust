package kr.hs.dgsw.trust.server.data.entity

import org.springframework.boot.configurationprocessor.json.JSONObject
import javax.persistence.*

@Entity(name = "account")
class Account {
    @Column(name = "name_")
    var name: String? = null
    @Id
    var username: String? = null
    var password: String? = null
    @Column(name = "profile_image")
    var profileImage: String? = null
}

fun Account.toJsonObject(): JSONObject {
    val jsonObject = JSONObject()
    jsonObject.put("name", name)
    jsonObject.put("username", username)
    jsonObject.put("profileImage", profileImage)
    return jsonObject
}