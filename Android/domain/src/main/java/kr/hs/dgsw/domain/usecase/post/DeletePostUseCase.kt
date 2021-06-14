package kr.hs.dgsw.domain.usecase.post

import io.reactivex.Single
import kr.hs.dgsw.domain.base.ParamsUseCase
import kr.hs.dgsw.domain.entity.Post
import kr.hs.dgsw.domain.repository.PostRepository
import javax.inject.Inject

class DeletePostUseCase @Inject constructor(
        private val repository: PostRepository
): ParamsUseCase<DeletePostUseCase.Params, Single<Post>>() {

    override fun buildUseCaseObservable(params: Params): Single<Post> {
        return repository.deletePost(params.postId)
    }

    data class Params(
            val postId: Int,
    )

}