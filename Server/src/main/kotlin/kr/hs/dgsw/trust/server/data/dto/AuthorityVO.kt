package kr.hs.dgsw.trust.server.data.dto

import kr.hs.dgsw.trust.server.data.entity.AuthorityDTO
import lombok.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table


@Entity(name = "authority")
class AuthorityVO {
    @Id
    @Column(name = "authority_name", length = 50)
    var authorityName: String? = null
}

fun AuthorityVO.toDTO(): AuthorityDTO {
    return AuthorityDTO(authorityName!!)
}