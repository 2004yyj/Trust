package kr.hs.dgsw.domain.repository

import io.reactivex.Single
import kr.hs.dgsw.domain.entity.Account

interface AccountRepository {

    fun postLogin(username: String, password: String) : Single<Account>

    fun postSignIn(name: String, username: String, password: String) : Single<Account>

}