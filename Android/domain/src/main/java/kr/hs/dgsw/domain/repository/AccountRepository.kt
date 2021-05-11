package kr.hs.dgsw.domain.repository

import io.reactivex.Single
import kr.hs.dgsw.domain.entity.Account
import okhttp3.MultipartBody

interface AccountRepository {

    fun postLogin(username: String, password: String) : Single<Account>

    fun postSignUp(name: String, username: String, password: String, profileImage: MultipartBody.Part?) : Single<Account>

}