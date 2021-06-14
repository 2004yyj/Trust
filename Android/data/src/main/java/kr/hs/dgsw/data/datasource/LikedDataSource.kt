package kr.hs.dgsw.data.datasource

import io.reactivex.Single
import kr.hs.dgsw.data.base.NoCacheDataSource
import kr.hs.dgsw.data.mapper.toEntity
import kr.hs.dgsw.data.network.remote.LikedRemote
import kr.hs.dgsw.domain.entity.Liked
import javax.inject.Inject

class LikedDataSource @Inject constructor(
        override val remote: LikedRemote
): NoCacheDataSource<LikedRemote>() {
    fun getAllLiked(postId: Int): Single<List<Liked>> {
        return remote.getAllLiked(postId).map { likedResponseList ->
            val likedList = ArrayList<Liked>()
            likedResponseList.forEach {
                likedList.add(it.toEntity())
            }
            likedList
        }
    }

    fun postLiked(postId: Int): Single<List<Liked>> {
        return remote.postLiked(postId).map { likedResponseList ->
            val likedList = ArrayList<Liked>()
            likedResponseList.forEach {
                likedList.add(it.toEntity())
            }
            likedList
        }
    }

    fun deleteLiked(postId: Int): Single<List<Liked>> {
        return remote.deleteLiked(postId).map { likedResponseList ->
            val likedList = ArrayList<Liked>()
            likedResponseList.forEach {
                likedList.add(it.toEntity())
            }
            likedList
        }
    }
}