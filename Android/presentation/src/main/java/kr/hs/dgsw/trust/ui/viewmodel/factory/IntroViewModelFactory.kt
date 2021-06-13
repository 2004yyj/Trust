package kr.hs.dgsw.trust.ui.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kr.hs.dgsw.domain.usecase.account.PostAutoLoginUseCase
import kr.hs.dgsw.trust.ui.viewmodel.activity.IntroViewModel

class IntroViewModelFactory(
    private val postAutoLoginUseCase: PostAutoLoginUseCase
) : ViewModelProvider.Factory  {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(IntroViewModel::class.java)) {
            IntroViewModel(postAutoLoginUseCase) as T
        } else {
            throw IllegalArgumentException()
        }
    }
}