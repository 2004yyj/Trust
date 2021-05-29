package kr.hs.dgsw.data.mapper

import kr.hs.dgsw.data.entity.LikedResponse
import kr.hs.dgsw.domain.entity.Liked

fun LikedResponse.toEntity(): Liked {
    return Liked(
            this.id,
            this.postId,
            this.createdAt,
            this.account.toEntity(),
    )
}

fun Liked.toResponse(): LikedResponse {
    return LikedResponse(
            this.id,
            this.postId,
            this.createdAt,
            this.account.toResponse(),
    )
}