package kr.hs.dgsw.trust.server.data.entity

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

fun Post.toHashMap(): HashMap<String, Any?> {
    val hashMap = HashMap<String, Any?>()
    hashMap["id"] = id
    hashMap["username"] = username
    hashMap["createdAt"] = createdAt?.time
    hashMap["content"] = content
    hashMap["imageList"] = imageList
    hashMap["isAnonymous"] = isAnonymous
    return hashMap
}