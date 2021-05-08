package kr.hs.dgsw.data.util

data class Response<T>(
        val status: String,
        val message: String,
        val data: T
)