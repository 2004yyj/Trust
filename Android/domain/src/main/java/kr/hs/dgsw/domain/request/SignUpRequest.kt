package kr.hs.dgsw.domain.request

import okhttp3.MultipartBody

data class SignUpRequest(
        val name: String,
        val username: String,
        val password: String,
        val profileImage: MultipartBody.Part?
)
