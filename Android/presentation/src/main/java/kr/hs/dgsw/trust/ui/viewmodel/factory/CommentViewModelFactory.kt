package kr.hs.dgsw.trust.ui.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kr.hs.dgsw.domain.usecase.comment.GetAllCommentUseCase
import kr.hs.dgsw.trust.ui.viewmodel.fragment.CommentViewModel

class CommentViewModelFactory(private val getAllCommentUseCase: GetAllCommentUseCase) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(CommentViewModel::class.java)) {
            CommentViewModel(getAllCommentUseCase) as T
        } else {
            throw IllegalArgumentException()
        }
    }
}