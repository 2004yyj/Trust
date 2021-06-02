package kr.hs.dgsw.data.entity

data class PostResponse(
    val id: Int,
    val createdAt: Long,
    val isAnonymous: Boolean,
    val content: String,
    val imageList: List<String>,
    val account: AccountResponse,
    val likedList: List<String>,
    val likedSize: Int
)