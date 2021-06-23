package kr.hs.dgsw.trust.server.data.vo

import kr.hs.dgsw.trust.server.data.dto.AuthorityDTO
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id


@Entity(name = "authority")
class AuthorityVO {
    @Id
    @Column(name = "authority_name", length = 50)
    var authorityName: String? = null
}

fun AuthorityVO.toDTO(): AuthorityDTO {
    return AuthorityDTO(authorityName!!)
}