package kr.hs.dgsw.domain.entity

data class Comment(
        val id: Int,
        val postId: Int,
        val createdAt: Long,
        val isAnonymous: Boolean,
        val content: String,
        val imageList: List<String>,
        val account: Account
)