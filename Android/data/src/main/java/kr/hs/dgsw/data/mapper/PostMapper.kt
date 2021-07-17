package kr.hs.dgsw.data.mapper

import kr.hs.dgsw.data.entity.PostResponse
import kr.hs.dgsw.domain.entity.Post

fun PostResponse.toEntity(): Post {
    return Post(
            this.id,
            this.createdAt,
            this.isAnonymous,
            this.content,
            this.imageList,
            this.account.toEntity(),
            this.isChecked,
            this.likedSize,
            this.admin
    )
}

fun Post.toResponse(): PostResponse {
    return PostResponse(
            this.id,
            this.createdAt,
            this.isAnonymous,
            this.content,
            this.imageList,
            this.account.toResponse(),
            this.isChecked,
            this.likedSize,
            this.admin
    )
}