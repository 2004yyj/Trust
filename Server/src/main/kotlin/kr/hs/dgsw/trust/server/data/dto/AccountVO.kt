package kr.hs.dgsw.trust.server.data.dto

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.boot.configurationprocessor.json.JSONObject
import javax.persistence.*
import kr.hs.dgsw.trust.server.data.entity.AccountDTO as AccountDTO


@Entity(name = "account")
class AccountVO {
    @Column(name = "name_")
    var name: String? = null
    @Id
    var username: String? = null
    var password: String? = null
    @Column(name = "profile_image")
    var profileImage: String? = null

    @JsonIgnore
    @Column(name = "activated")
    var activated = false

    @ManyToMany
    @JoinTable(
        name = "user_authority",
        joinColumns = [JoinColumn(name = "username", referencedColumnName = "username")],
        inverseJoinColumns = [JoinColumn(name = "authority_name", referencedColumnName = "authority_name")]
    )

    var authorities: Set<AuthorityVO>? = null
}

fun AccountVO.toDTO(): AccountDTO {
    return AccountDTO(
        this.name!!,
        this.username!!,
        this.profileImage,
    )
}