package kr.hs.dgsw.trust.server.data.entity

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

fun Liked.toHashMap(): HashMap<String, Any?> {
    val hashMap = HashMap<String, Any?>()
    hashMap["id"] = id
    hashMap["postId"] = postId
    hashMap["username"] = username
    hashMap["createdAt"] = createdAt?.time
    return hashMap
}