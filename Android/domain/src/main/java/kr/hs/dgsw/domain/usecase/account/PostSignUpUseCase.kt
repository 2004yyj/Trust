package kr.hs.dgsw.domain.usecase.account

import io.reactivex.Single
import kr.hs.dgsw.domain.base.ParamsUseCase
import kr.hs.dgsw.domain.entity.Token
import kr.hs.dgsw.domain.repository.AccountRepository
import kr.hs.dgsw.domain.request.SignUpRequest
import javax.inject.Inject

class PostSignUpUseCase @Inject constructor(
        private val repository: AccountRepository
): ParamsUseCase<PostSignUpUseCase.Params, Single<Token>>() {

    override fun buildUseCaseObservable(params: Params) : Single<Token> {
        return repository.postSignUp(params.signUpRequest)
    }

    data class Params(
            val signUpRequest: SignUpRequest
    )

}