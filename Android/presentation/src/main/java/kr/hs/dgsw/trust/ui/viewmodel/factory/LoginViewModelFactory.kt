package kr.hs.dgsw.trust.ui.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kr.hs.dgsw.domain.usecase.account.PostLoginUseCase
import kr.hs.dgsw.trust.ui.viewmodel.fragment.LoginViewModel

class LoginViewModelFactory(private val postLoginUseCase: PostLoginUseCase) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            LoginViewModel(postLoginUseCase) as T
        } else {
            throw IllegalArgumentException()
        }
    }
}