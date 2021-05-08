package kr.hs.dgsw.domain.usecase.account

import io.reactivex.Single
import kr.hs.dgsw.domain.entity.Account
import kr.hs.dgsw.domain.repository.AccountRepository
import javax.inject.Inject

class PostLoginUseCase @Inject constructor(private val repository: AccountRepository) {

    fun postLogin(username: String, password: String) : Single<Account> {
        return repository.postLogin(username, password)
    }

}