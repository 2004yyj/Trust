package kr.hs.dgsw.domain.entity

data class Post(
        val id: Int,
        val account: Account,
        val createdAt: Long,
        val content: String,
)