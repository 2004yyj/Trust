package kr.hs.dgsw.trust.server.data.entity

import javax.persistence.*

@Entity(name = "account")
class Account {
    @Column(name = "name_")
    var name: String? = null
    @Id
    var username: String? = null
    var password: String? = null
    var profileImage: String? = null
}