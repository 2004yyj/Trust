package kr.hs.dgsw.data.entity

data class PostResponse(
        val id: Int,
        val account: AccountResponse,
        val createdAt: Long,
        val content: String
)