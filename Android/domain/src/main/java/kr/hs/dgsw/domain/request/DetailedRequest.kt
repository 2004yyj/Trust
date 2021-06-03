package kr.hs.dgsw.domain.request

data class DetailedRequest(
        val postId: Int,
        val username: String,
        val password: String
)