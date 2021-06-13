package kr.hs.dgsw.domain.usecase.account

import io.reactivex.Single
import kr.hs.dgsw.domain.base.BaseUseCase
import kr.hs.dgsw.domain.entity.Token
import kr.hs.dgsw.domain.repository.AccountRepository
import javax.inject.Inject

class PostAutoLoginUseCase @Inject constructor(
    private val accountRepository: AccountRepository
) : BaseUseCase<Single<Token>>() {
    override fun buildUseCaseObservable(): Single<Token> {
        return accountRepository.postAutoLogin()
    }
}