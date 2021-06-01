package kr.hs.dgsw.data.network.remote

import com.google.gson.Gson
import io.reactivex.Single
import kr.hs.dgsw.data.base.BaseRemote
import kr.hs.dgsw.data.entity.AccountResponse
import kr.hs.dgsw.data.network.service.AccountService
import kr.hs.dgsw.data.util.Response
import kr.hs.dgsw.domain.request.LoginRequest
import kr.hs.dgsw.domain.request.SignUpRequest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class AccountRemote @Inject constructor(
        override val service: AccountService,
): BaseRemote<AccountService>() {

    fun postLogin(loginRequest: LoginRequest): Single<AccountResponse> {
        return service.postLogin(loginRequest.username, loginRequest.password).map(getResponse())
    }

    fun postSignUp(signUpRequest: SignUpRequest): Single<AccountResponse> {
        val textType = "text/plain".toMediaType()
        val nameBody = signUpRequest.name.toRequestBody(textType)
        val usernameBody = signUpRequest.username.toRequestBody(textType)
        val passwordBody = signUpRequest.password.toRequestBody(textType)

        return service.postSignUp(nameBody, usernameBody, passwordBody, signUpRequest.profileImage).map(getResponse())
    }

}