package kr.hs.dgsw.trust.ui.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kr.hs.dgsw.domain.usecase.post.PostPostUseCase
import kr.hs.dgsw.trust.ui.viewmodel.fragment.AddViewModel

class AddViewModelFactory(
    private val postPostUseCase: PostPostUseCase
): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(AddViewModel::class.java)) {
            AddViewModel(postPostUseCase) as T
        } else {
            throw IllegalArgumentException()
        }
    }

}
