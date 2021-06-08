package kr.hs.dgsw.domain.repository

import io.reactivex.Single
import kr.hs.dgsw.domain.entity.Liked

interface LikedRepository {
    fun getAllLiked(postId: Int): Single<List<Liked>>
    fun postLiked(postId: Int, username: String, password: String): Single<List<Liked>>
    fun deleteLiked(id: Int, username: String, password: String): Single<List<Liked>>
}