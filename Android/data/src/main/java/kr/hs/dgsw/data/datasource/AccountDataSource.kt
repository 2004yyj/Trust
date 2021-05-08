package kr.hs.dgsw.data.datasource

import io.reactivex.Single
import kr.hs.dgsw.data.mapper.toEntity
import kr.hs.dgsw.data.network.remote.AccountRemote
import kr.hs.dgsw.domain.entity.Account
import javax.inject.Inject

class AccountDataSource @Inject constructor(private val accountRemote: AccountRemote) {

    fun postLogin(username: String, password: String) : Single<Account> {
        return accountRemote.postLogin(username, password).map { it.toEntity() }
    }

    fun postSignIn(name: String, username: String, password: String) : Single<Account> {
        return accountRemote.postSignIn(name, username, password).map { it.toEntity() }
    }

}