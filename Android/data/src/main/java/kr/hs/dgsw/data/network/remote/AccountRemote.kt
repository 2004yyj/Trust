package kr.hs.dgsw.data.network.remote

import android.util.Log
import com.google.gson.Gson
import io.reactivex.Single
import kr.hs.dgsw.data.entity.AccountResponse
import kr.hs.dgsw.data.network.service.AccountService
import kr.hs.dgsw.data.util.Response
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.internal.userAgent
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

    fun postSignUp(name: String, username: String, password: String, profileImage: MultipartBody.Part?): Single<AccountResponse> {
        return accountService.postSignUp(name, username, password, profileImage).map {
            if (it.isSuccessful) {
                it.body()!!.data
            } else {
                val errorBody = gson.fromJson(it.errorBody()!!.charStream(), Response::class.java)
                throw Throwable(errorBody.message)
            }
        }
    }

}