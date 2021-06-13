package kr.hs.dgsw.data.repository

import io.reactivex.Single
import kr.hs.dgsw.data.datasource.AccountDataSource
import kr.hs.dgsw.domain.entity.Token
import kr.hs.dgsw.domain.repository.AccountRepository
import kr.hs.dgsw.domain.request.LoginRequest
import kr.hs.dgsw.domain.request.SignUpRequest
import javax.inject.Inject

class AccountRepositoryImpl @Inject constructor(private val accountDataSource: AccountDataSource) : AccountRepository {

    override fun postAutoLogin(): Single<Token> {
        return accountDataSource.postAutoLogin()
    }

    override fun postLogin(loginRequest: LoginRequest): Single<Token> {
        return accountDataSource.postLogin(loginRequest)
    }

    override fun postSignUp(signUpRequest: SignUpRequest): Single<Token> {
        return accountDataSource.postSignUp(signUpRequest)
    }

}