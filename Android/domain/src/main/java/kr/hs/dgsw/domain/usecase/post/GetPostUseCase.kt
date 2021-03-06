package kr.hs.dgsw.domain.usecase.post

import io.reactivex.Single
import kr.hs.dgsw.domain.base.ParamsUseCase
import kr.hs.dgsw.domain.entity.Post
import kr.hs.dgsw.domain.repository.PostRepository
import javax.inject.Inject

class GetPostUseCase @Inject constructor(
        private val repository: PostRepository
): ParamsUseCase<GetPostUseCase.Params, Single<Post>>() {

    override fun buildUseCaseObservable(params: Params): Single<Post> {
        return repository.getPost(params.id)
    }

    data class Params(
            val id: Int
    )

}