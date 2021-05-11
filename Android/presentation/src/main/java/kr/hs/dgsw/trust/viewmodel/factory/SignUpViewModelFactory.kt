package kr.hs.dgsw.trust.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kr.hs.dgsw.domain.usecase.account.PostSignUpUseCase
import kr.hs.dgsw.trust.viewmodel.fragment.SignUpViewModel

class SignUpViewModelFactory(private val postSignUpUseCase: PostSignUpUseCase) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(SignUpViewModel::class.java)) {
            SignUpViewModel(postSignUpUseCase) as T
        } else {
            throw IllegalArgumentException()
        }
    }
}