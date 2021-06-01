package kr.hs.dgsw.domain.usecase.post

import io.reactivex.Single
import kr.hs.dgsw.domain.base.ParamsUseCase
import kr.hs.dgsw.domain.entity.Post
import kr.hs.dgsw.domain.repository.PostRepository
import javax.inject.Inject

class GetAllPostByUsernameUseCase @Inject constructor(
        private val repository: PostRepository
): ParamsUseCase<GetAllPostByUsernameUseCase.Params, Single<List<Post>>>() {
    override fun buildUseCaseObservable(params: Params): Single<List<Post>> {
        return repository.getAllPostByUsername(params.username)
    }

    data class Params(
            val username: String
    )
}