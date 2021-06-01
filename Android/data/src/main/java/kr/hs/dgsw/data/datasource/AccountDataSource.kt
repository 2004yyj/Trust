package kr.hs.dgsw.data.datasource

import io.reactivex.Single
import kr.hs.dgsw.data.mapper.toEntity
import kr.hs.dgsw.data.network.remote.AccountRemote
import kr.hs.dgsw.domain.entity.Account
import kr.hs.dgsw.domain.request.LoginRequest
import kr.hs.dgsw.domain.request.SignUpRequest
import okhttp3.MultipartBody
import javax.inject.Inject

class AccountDataSource @Inject constructor(private val accountRemote: AccountRemote) {

    fun postLogin(loginRequest: LoginRequest) : Single<Account> {
        return accountRemote.postLogin(loginRequest).map { it.toEntity() }
    }

    fun postSignUp(signUpRequest: SignUpRequest) : Single<Account> {
        return accountRemote.postSignUp(signUpRequest).map { it.toEntity() }
    }

}