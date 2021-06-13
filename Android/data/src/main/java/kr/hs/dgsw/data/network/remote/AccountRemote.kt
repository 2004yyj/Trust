package kr.hs.dgsw.data.network.remote

import io.reactivex.Single
import kr.hs.dgsw.data.base.BaseRemote
import kr.hs.dgsw.data.entity.TokenResponse
import kr.hs.dgsw.data.network.service.AccountService
import kr.hs.dgsw.domain.request.LoginRequest
import kr.hs.dgsw.domain.request.SignUpRequest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class AccountRemote @Inject constructor(
        override val service: AccountService,
): BaseRemote<AccountService>() {

    fun postAutoLogin(): Single<TokenResponse> {
        return service.postAutoLogin().map(getResponse())
    }

    fun postLogin(loginRequest: LoginRequest): Single<TokenResponse> {
        return service.postLogin(loginRequest.username, loginRequest.password).map(getResponse())
    }

    fun postSignUp(signUpRequest: SignUpRequest): Single<TokenResponse> {
        val textType = "text/plain".toMediaType()
        val nameBody = signUpRequest.name.toRequestBody(textType)
        val usernameBody = signUpRequest.username.toRequestBody(textType)
        val passwordBody = signUpRequest.password.toRequestBody(textType)

        return service.postSignUp(nameBody, usernameBody, passwordBody, signUpRequest.profileImage).map(getResponse())
    }

}