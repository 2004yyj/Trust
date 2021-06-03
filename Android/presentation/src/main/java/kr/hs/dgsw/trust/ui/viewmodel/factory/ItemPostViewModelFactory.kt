package kr.hs.dgsw.trust.ui.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kr.hs.dgsw.domain.usecase.liked.DeleteLikedUseCase
import kr.hs.dgsw.domain.usecase.liked.PostLikedUseCase
import kr.hs.dgsw.trust.ui.viewmodel.adapter.ItemPostViewModel

class ItemPostViewModelFactory(
        private val postLikedUseCase: PostLikedUseCase,
        private val deleteLikedUseCase: DeleteLikedUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(ItemPostViewModel::class.java)) {
            ItemPostViewModel(postLikedUseCase, deleteLikedUseCase) as T
        } else {
            throw IllegalArgumentException()
        }
    }
}