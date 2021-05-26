package kr.hs.dgsw.data.mapper

import kr.hs.dgsw.data.entity.CommentResponse
import kr.hs.dgsw.domain.entity.Comment

fun CommentResponse.toEntity(): Comment {
    return Comment(
            this.postId,
            this.account.toEntity(),
            this.content
    )
}

fun Comment.toResponse(): CommentResponse {
    return CommentResponse(
            this.postId,
            this.account.toResponse(),
            this.content
    )
}