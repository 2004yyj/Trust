package kr.hs.dgsw.domain.usecase.comment

import io.reactivex.Single
import kr.hs.dgsw.domain.base.ParamsUseCase
import kr.hs.dgsw.domain.entity.Comment
import kr.hs.dgsw.domain.repository.CommentRepository
import okhttp3.MultipartBody
import javax.inject.Inject

class PostCommentUseCase @Inject constructor(
        private val commentRepository: CommentRepository
) : ParamsUseCase<PostCommentUseCase.Params, Single<List<Comment>>>() {

    override fun buildUseCaseObservable(params: Params): Single<List<Comment>> {
        return commentRepository.postComment(
                params.postId,
                params.username,
                params.password,
                params.content,
                params.isAnonymous,
                params.imageList
        )
    }

    data class Params(
            val postId: Int,
            val username: String,
            val password: String,
            val content: String,
            val isAnonymous: Boolean,
            val imageList: List<MultipartBody.Part>?
    )
}