package kr.hs.dgsw.domain.entity

data class Liked(
        val id: Int,
        val postId: Int,
        val createdAt: Long,
        val account: Account
)
