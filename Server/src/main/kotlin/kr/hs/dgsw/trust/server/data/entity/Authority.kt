package kr.hs.dgsw.trust.server.data.entity

import lombok.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table


@Entity(name = "authority")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
class Authority {
    @Id
    @Column(name = "authority_name", length = 50)
    var authorityName: String? = null
}
