package kr.hs.dgsw.domain.repository

import io.reactivex.Single
import kr.hs.dgsw.domain.entity.Token
import kr.hs.dgsw.domain.request.LoginRequest
import kr.hs.dgsw.domain.request.SignUpRequest

interface AccountRepository {

    fun postAutoLogin() : Single<Token>

    fun postLogin(loginRequest: LoginRequest) : Single<Token>

    fun postSignUp(signUpRequest: SignUpRequest) : Single<Token>

}