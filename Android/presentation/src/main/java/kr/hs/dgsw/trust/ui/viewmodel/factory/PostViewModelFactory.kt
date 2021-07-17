package kr.hs.dgsw.trust.ui.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kr.hs.dgsw.domain.usecase.post.DeletePostUseCase
import kr.hs.dgsw.domain.usecase.post.GetAllPostUseCase
import kr.hs.dgsw.domain.usecase.post.UpdatePostUseCase
import kr.hs.dgsw.trust.ui.viewmodel.fragment.PostViewModel

class PostViewModelFactory(
    private val getAllPostUseCase: GetAllPostUseCase,
    private val updatePostUseCase: UpdatePostUseCase,
    private val deletePostUseCase: DeletePostUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(PostViewModel::class.java)) {
            PostViewModel(getAllPostUseCase, updatePostUseCase, deletePostUseCase) as T
        } else {
            throw IllegalArgumentException()
        }
    }
}