package kr.hs.dgsw.trust.ui.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kr.hs.dgsw.domain.usecase.post.DeletePostUseCase
import kr.hs.dgsw.domain.usecase.post.GetAllPostUseCase
import kr.hs.dgsw.domain.usecase.post.GetPostUseCase
import kr.hs.dgsw.trust.ui.viewmodel.fragment.PostViewModel

class PostViewModelFactory(
    private val getAllPostUseCase: GetAllPostUseCase,
    private val deletePostUseCase: DeletePostUseCase,
    private val getPostUseCase: GetPostUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(PostViewModel::class.java)) {
            PostViewModel(getAllPostUseCase, deletePostUseCase, getPostUseCase) as T
        } else {
            throw IllegalArgumentException()
        }
    }
}