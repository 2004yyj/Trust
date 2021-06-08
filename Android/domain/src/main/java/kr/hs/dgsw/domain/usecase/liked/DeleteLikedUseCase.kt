package kr.hs.dgsw.domain.usecase.liked

import io.reactivex.Single
import kr.hs.dgsw.domain.base.ParamsUseCase
import kr.hs.dgsw.domain.entity.Liked
import kr.hs.dgsw.domain.repository.LikedRepository
import kr.hs.dgsw.domain.request.DetailedRequest
import javax.inject.Inject

class DeleteLikedUseCase @Inject constructor(
        private val likedRepository: LikedRepository
): ParamsUseCase<DeleteLikedUseCase.Params, Single<List<Liked>>>() {

    override fun buildUseCaseObservable(params: Params): Single<List<Liked>> {
        return likedRepository.deleteLiked(
                params.postId,
                params.username,
                params.password
        )
    }

    data class Params(
            val postId: Int,
            val username: String,
            val password: String
    )
}