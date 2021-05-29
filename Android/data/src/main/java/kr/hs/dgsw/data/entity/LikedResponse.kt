package kr.hs.dgsw.data.entity

data class LikedResponse(
    val id: Int,
    val postId: Int,
    val createdAt: Long,
    val account: AccountResponse
)