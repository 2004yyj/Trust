package kr.hs.dgsw.data.datasource

import io.reactivex.Single
import kr.hs.dgsw.data.mapper.toEntity
import kr.hs.dgsw.data.network.remote.AccountRemote
import kr.hs.dgsw.domain.entity.Account
import okhttp3.MultipartBody
import javax.inject.Inject

class AccountDataSource @Inject constructor(private val accountRemote: AccountRemote) {

    fun postLogin(username: String, password: String) : Single<Account> {
        return accountRemote.postLogin(username, password).map { it.toEntity() }
    }

    fun postSignUp(name: String, username: String, password: String, profileImage: MultipartBody.Part?) : Single<Account> {
        return accountRemote.postSignUp(name, username, password, profileImage).map { it.toEntity() }
    }

}