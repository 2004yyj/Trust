package kr.hs.dgsw.trust.ui.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kr.hs.dgsw.domain.usecase.post.UpdatePostUseCase
import kr.hs.dgsw.trust.ui.viewmodel.fragment.UpdateViewModel

class UpdateViewModelFactory(
    private val updatePostUseCase: UpdatePostUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(UpdateViewModel::class.java)) {
            UpdateViewModel(updatePostUseCase) as T
        } else {
            throw IllegalArgumentException()
        }
    }

}
