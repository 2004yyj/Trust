package kr.hs.dgsw.data.mapper

import kr.hs.dgsw.data.entity.AccountResponse
import kr.hs.dgsw.data.entity.CommentResponse
import kr.hs.dgsw.domain.entity.Comment

fun CommentResponse.toEntity(): Comment {
    return Comment(
            this.id,
            this.postId,
            this.createdAt,
            this.isAnonymous,
            this.content,
            this.imageList,
            this.account.toEntity()
    )
}

fun Comment.toResponse(): CommentResponse {
    return CommentResponse(
            this.id,
            this.postId,
            this.createdAt,
            this.isAnonymous,
            this.content,
            this.imageList,
            this.account.toResponse()
    )
}