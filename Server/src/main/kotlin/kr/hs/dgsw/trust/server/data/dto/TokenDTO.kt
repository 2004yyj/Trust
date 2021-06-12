package kr.hs.dgsw.trust.server.data.dto

import lombok.*

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
class TokenDTO(val token: String, val username: String)