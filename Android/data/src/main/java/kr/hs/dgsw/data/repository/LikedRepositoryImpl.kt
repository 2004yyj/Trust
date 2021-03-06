package kr.hs.dgsw.data.repository

import io.reactivex.Single
import kr.hs.dgsw.data.datasource.LikedDataSource
import kr.hs.dgsw.domain.entity.Liked
import kr.hs.dgsw.domain.repository.LikedRepository
import javax.inject.Inject

class LikedRepositoryImpl @Inject constructor(
        private val likedDataSource: LikedDataSource
) : LikedRepository {
    override fun getAllLiked(postId: Int): Single<List<Liked>> {
        return likedDataSource.getAllLiked(postId)
    }

    override fun postLiked(postId: Int): Single<List<Liked>> {
        return likedDataSource.postLiked(postId)
    }

    override fun deleteLiked(id: Int): Single<List<Liked>> {
        return likedDataSource.deleteLiked(id)
    }
}