package kr.hs.dgsw.domain.entity

data class Comment(
        val postId: Int,
        val account: Account,
        val content: String,
)