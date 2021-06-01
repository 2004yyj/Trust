package kr.hs.dgsw.domain.usecase.account

import io.reactivex.Single
import kr.hs.dgsw.domain.base.ParamsUseCase
import kr.hs.dgsw.domain.entity.Account
import kr.hs.dgsw.domain.repository.AccountRepository
import kr.hs.dgsw.domain.request.SignUpRequest
import okhttp3.MultipartBody
import javax.inject.Inject

class PostSignUpUseCase @Inject constructor(
        private val repository: AccountRepository
): ParamsUseCase<PostSignUpUseCase.Params, Single<Account>>() {

    override fun buildUseCaseObservable(params: Params) : Single<Account> {
        return repository.postSignUp(params.signUpRequest)
    }

    data class Params(
            val signUpRequest: SignUpRequest
    )

}