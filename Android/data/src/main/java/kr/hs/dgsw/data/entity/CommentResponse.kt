package kr.hs.dgsw.data.entity

data class CommentResponse(
    val id: Int,
    val postId: Int,
    val createdAt: Long,
    val isAnonymous: Boolean,
    val content: String,
    val imageList: List<String>,
    val account: AccountResponse
)