package kr.hs.dgsw.domain.usecase.account

import io.reactivex.Single
import kr.hs.dgsw.domain.base.ParamsUseCase
import kr.hs.dgsw.domain.entity.Account
import kr.hs.dgsw.domain.repository.AccountRepository
import kr.hs.dgsw.domain.request.LoginRequest
import javax.inject.Inject

class PostLoginUseCase @Inject constructor(
        private val repository: AccountRepository
): ParamsUseCase<PostLoginUseCase.Params, Single<Account>>() {

    override fun buildUseCaseObservable(params: Params) : Single<Account> {
        return repository.postLogin(params.loginRequest)
    }

    data class Params(
            val loginRequest: LoginRequest
    )

}