package kr.hs.dgsw.data.mapper

import kr.hs.dgsw.data.entity.LikedResponse
import kr.hs.dgsw.domain.entity.Liked

fun LikedResponse.toEntity(): Liked {
    return Liked(
            this.postId,
            this.account.toEntity(),
    )
}

fun Liked.toResponse(): LikedResponse {
    return LikedResponse(
            this.postId,
            this.account.toResponse(),
    )
}