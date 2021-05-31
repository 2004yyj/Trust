package kr.hs.dgsw.trust.ui.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kr.hs.dgsw.domain.usecase.account.PostSignUpUseCase
import kr.hs.dgsw.domain.usecase.post.GetPostUseCase
import kr.hs.dgsw.trust.ui.viewmodel.fragment.PostViewModel
import kr.hs.dgsw.trust.ui.viewmodel.fragment.SignUpViewModel

class PostViewModelFactory(private val getPostUseCase: GetPostUseCase) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(PostViewModel::class.java)) {
            PostViewModel(getPostUseCase) as T
        } else {
            throw IllegalArgumentException()
        }
    }
}