package kr.hs.dgsw.data.mapper

import kr.hs.dgsw.data.entity.PostResponse
import kr.hs.dgsw.domain.entity.Post

fun PostResponse.toEntity(): Post {
    return Post(
            this.id,
            this.account.toEntity(),
            this.createdAt,
            this.content
    )
}

fun Post.toResponse(): PostResponse {
    return PostResponse(
            this.id,
            this.account.toResponse(),
            this.createdAt,
            this.content
    )
}