package kr.hs.dgsw.domain.usecase.comment

import io.reactivex.Single
import kr.hs.dgsw.domain.base.ParamsUseCase
import kr.hs.dgsw.domain.entity.Comment
import kr.hs.dgsw.domain.repository.CommentRepository
import javax.inject.Inject

class GetAllCommentUseCase @Inject constructor(
        private val commentRepository: CommentRepository
) : ParamsUseCase<GetAllCommentUseCase.Params, Single<List<Comment>>>() {

    override fun buildUseCaseObservable(params: Params): Single<List<Comment>> {
        return commentRepository.getAllComment(params.postId)
    }

    data class Params(
            val postId: Int
    )
}