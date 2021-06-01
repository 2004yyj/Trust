package kr.hs.dgsw.domain.repository

import io.reactivex.Single
import kr.hs.dgsw.domain.entity.Account
import kr.hs.dgsw.domain.request.LoginRequest
import kr.hs.dgsw.domain.request.SignUpRequest
import okhttp3.MultipartBody

interface AccountRepository {

    fun postLogin(loginRequest: LoginRequest) : Single<Account>

    fun postSignUp(signUpRequest: SignUpRequest) : Single<Account>

}