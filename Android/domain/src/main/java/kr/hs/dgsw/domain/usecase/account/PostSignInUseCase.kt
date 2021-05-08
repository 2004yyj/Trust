package kr.hs.dgsw.domain.usecase.account

import io.reactivex.Single
import kr.hs.dgsw.domain.entity.Account
import kr.hs.dgsw.domain.repository.AccountRepository
import javax.inject.Inject

class PostSignInUseCase @Inject constructor(private val repository: AccountRepository) {

    fun postSignIn(name: String, username: String, password: String) : Single<Account> {
        return repository.postSignIn(name, username, password)
    }

}