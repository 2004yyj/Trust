package kr.hs.dgsw.domain.entity

data class Post(
        val id: Int,
        val createdAt: Long,
        val isAnonymous: Boolean,
        val content: String,
        val imageList: List<String>,
        val account: Account,
        val likedList: List<String>,
        val likedSize: Int
)