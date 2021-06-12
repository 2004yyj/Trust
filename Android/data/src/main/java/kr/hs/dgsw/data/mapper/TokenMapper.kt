package kr.hs.dgsw.data.mapper

import kr.hs.dgsw.data.entity.TokenResponse
import kr.hs.dgsw.domain.entity.Token

fun TokenResponse.toEntity(): Token {
    return Token(
        this.token,
        this.username,
    )
}

fun Token.toResponse(): TokenResponse {
    return TokenResponse(
        this.token,
        this.username,
    )
}