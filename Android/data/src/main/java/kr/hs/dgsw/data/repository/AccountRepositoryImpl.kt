package kr.hs.dgsw.data.repository

import io.reactivex.Single
import kr.hs.dgsw.data.datasource.AccountDataSource
import kr.hs.dgsw.domain.entity.Account
import kr.hs.dgsw.domain.repository.AccountRepository
import kr.hs.dgsw.domain.request.LoginRequest
import kr.hs.dgsw.domain.request.SignUpRequest
import okhttp3.MultipartBody
import javax.inject.Inject

class AccountRepositoryImpl @Inject constructor(private val accountDataSource: AccountDataSource) : AccountRepository {

    override fun postLogin(loginRequest: LoginRequest): Single<Account> {
        return accountDataSource.postLogin(loginRequest)
    }

    override fun postSignUp(signUpRequest: SignUpRequest): Single<Account> {
        return accountDataSource.postSignUp(signUpRequest)
    }

}