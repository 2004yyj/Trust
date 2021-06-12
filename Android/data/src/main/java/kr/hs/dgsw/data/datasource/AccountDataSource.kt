package kr.hs.dgsw.data.datasource

import io.reactivex.Single
import kr.hs.dgsw.data.base.NoCacheDataSource
import kr.hs.dgsw.data.mapper.toEntity
import kr.hs.dgsw.data.network.remote.AccountRemote
import kr.hs.dgsw.domain.entity.Token
import kr.hs.dgsw.domain.request.LoginRequest
import kr.hs.dgsw.domain.request.SignUpRequest
import javax.inject.Inject

class AccountDataSource @Inject constructor(
        override val remote: AccountRemote,
): NoCacheDataSource<AccountRemote>() {

    fun postLogin(loginRequest: LoginRequest) : Single<Token> {
        return remote.postLogin(loginRequest).map { it.toEntity() }
    }

    fun postSignUp(signUpRequest: SignUpRequest) : Single<Token> {
        return remote.postSignUp(signUpRequest).map { it.toEntity() }
    }

}