package kr.hs.dgsw.data.mapper

import kr.hs.dgsw.data.entity.AccountResponse
import kr.hs.dgsw.domain.entity.Account

fun AccountResponse.toEntity(): Account {
    return Account(
        this.name,
        this.username,
        this.profileImage
    )
}

fun Account.toResponse(): AccountResponse {
    return AccountResponse(
        this.name,
        this.username,
        this.profileImage
    )
}