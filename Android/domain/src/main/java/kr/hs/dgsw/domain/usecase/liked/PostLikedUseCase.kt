package kr.hs.dgsw.domain.usecase.liked

import io.reactivex.Single
import kr.hs.dgsw.domain.base.ParamsUseCase
import kr.hs.dgsw.domain.entity.Liked
import kr.hs.dgsw.domain.repository.LikedRepository
import javax.inject.Inject

class PostLikedUseCase @Inject constructor(
        private val likedRepository: LikedRepository
): ParamsUseCase<PostLikedUseCase.Params, Single<List<Liked>>>() {

    override fun buildUseCaseObservable(params: Params): Single<List<Liked>> {
        return likedRepository.postLiked(
                params.postId,
        )
    }

    data class Params(
            val postId: Int,
    )
}