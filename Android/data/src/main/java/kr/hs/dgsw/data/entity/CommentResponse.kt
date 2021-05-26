package kr.hs.dgsw.data.entity

data class CommentResponse(
        val postId: Int,
        val account: AccountResponse,
        val content: String,
)