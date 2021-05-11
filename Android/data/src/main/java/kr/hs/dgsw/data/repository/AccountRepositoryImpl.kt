package kr.hs.dgsw.data.repository

import io.reactivex.Single
import kr.hs.dgsw.data.datasource.AccountDataSource
import kr.hs.dgsw.domain.entity.Account
import kr.hs.dgsw.domain.repository.AccountRepository
import okhttp3.MultipartBody
import javax.inject.Inject

class AccountRepositoryImpl @Inject constructor(private val accountDataSource: AccountDataSource) : AccountRepository {

    override fun postLogin(username: String, password: String): Single<Account> {
        return accountDataSource.postLogin(username, password)
    }

    override fun postSignUp(name: String, username: String, password: String, profileImage: MultipartBody.Part?): Single<Account> {
        return accountDataSource.postSignUp(name, username, password, profileImage)
    }

}