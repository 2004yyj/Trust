package kr.hs.dgsw.domain.usecase.comment

import io.reactivex.Single
import kr.hs.dgsw.domain.base.ParamsUseCase
import kr.hs.dgsw.domain.entity.Comment
import kr.hs.dgsw.domain.repository.CommentRepository
import okhttp3.MultipartBody
import javax.inject.Inject

class DeleteCommentUseCase @Inject constructor(
        private val commentRepository: CommentRepository
) : ParamsUseCase<DeleteCommentUseCase.Params, Single<List<Comment>>>() {

    override fun buildUseCaseObservable(params: Params): Single<List<Comment>> {
        return commentRepository.deleteComment(
                params.postId,
                params.username,
                params.password,
        )
    }

    data class Params(
            val postId: Int,
            val username: String,
            val password: String
    )
}