package kr.hs.dgsw.data.network.remote

import com.google.gson.Gson
import io.reactivex.Single
import kr.hs.dgsw.data.entity.AccountResponse
import kr.hs.dgsw.data.network.service.AccountService
import kr.hs.dgsw.data.util.Response
import javax.inject.Inject

class AccountRemote @Inject constructor(
        private val accountService: AccountService,
        private val gson: Gson
) {

    fun postLogin(username: String, password: String): Single<AccountResponse> {
        return accountService.postLogin(username, password).map {
            if (it.isSuccessful) {
                it.body()!!.data
            } else {
                val errorBody = gson.fromJson(it.errorBody()!!.charStream(), Response::class.java)
                throw Throwable(errorBody.message)
            }
        }
    }

    fun postSignIn(name: String, username: String, password: String): Single<AccountResponse> {
        return accountService.postSignIn(name, username, password).map {
            if (it.isSuccessful) {
                it.body()!!.data
            } else {
                throw Throwable(it.body()!!.message)
            }
        }
    }

}