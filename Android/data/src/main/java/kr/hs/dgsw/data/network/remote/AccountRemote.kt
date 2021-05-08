package kr.hs.dgsw.data.network.remote

import io.reactivex.Single
import kr.hs.dgsw.data.entity.AccountResponse
import kr.hs.dgsw.data.network.service.AccountService
import javax.inject.Inject

class AccountRemote @Inject constructor(private val accountService: AccountService) {

    fun postLogin(username: String, password: String): Single<AccountResponse> {
        return accountService.postLogin(username, password).map {
            if (it.isSuccessful) {
                it.body()!!.data
            } else {
                throw Throwable(it.code().toString())
            }
        }
    }

    fun postSignIn(name: String, username: String, password: String): Single<AccountResponse> {
        return accountService.postSignIn(name, username, password).map {
            if (it.isSuccessful) {
                it.body()!!.data
            } else {
                throw Throwable(it.code().toString())
            }
        }
    }

}