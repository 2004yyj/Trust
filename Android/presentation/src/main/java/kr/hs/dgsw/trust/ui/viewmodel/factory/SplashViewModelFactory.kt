package kr.hs.dgsw.trust.ui.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kr.hs.dgsw.domain.usecase.account.PostAutoLoginUseCase
import kr.hs.dgsw.trust.ui.viewmodel.activity.SplashViewModel

class SplashViewModelFactory(
    private val postAutoLoginUseCase: PostAutoLoginUseCase
) : ViewModelProvider.Factory  {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(SplashViewModel::class.java)) {
            SplashViewModel(postAutoLoginUseCase) as T
        } else {
            throw IllegalArgumentException()
        }
    }
}