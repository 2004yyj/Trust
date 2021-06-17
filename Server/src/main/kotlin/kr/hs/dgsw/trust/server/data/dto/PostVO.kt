package kr.hs.dgsw.trust.server.data.dto

import kr.hs.dgsw.trust.server.data.entity.PostDTO
import org.springframework.boot.configurationprocessor.json.JSONArray
import org.springframework.boot.configurationprocessor.json.JSONObject
import java.sql.Timestamp
import javax.persistence.*

@Entity(name = "post")
class PostVO {
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

fun PostVO.toDTO(): PostDTO {
    return PostDTO(
        this.id!!,
        this.username!!,
        this.createdAt!!,
        this.content!!,
        this.imageList!!,
        this.isAnonymous!!
    )
}