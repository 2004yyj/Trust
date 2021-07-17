package kr.hs.dgsw.trust.server.data.vo

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.*
import kr.hs.dgsw.trust.server.data.dto.AccountDTO as AccountDTO


@Entity(name = "account")
class AccountVO {
    @Column(name = "name_")
    var name: String? = null
    @Id
    @Column(name = "username")
    var username: String? = null
    @Column(name = "password")
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