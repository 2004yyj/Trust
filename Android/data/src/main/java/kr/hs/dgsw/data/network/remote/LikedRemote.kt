package kr.hs.dgsw.data.network.remote

import io.reactivex.Single
import kr.hs.dgsw.data.base.BaseRemote
import kr.hs.dgsw.data.entity.LikedResponse
import kr.hs.dgsw.data.network.service.LikedService
import javax.inject.Inject

class LikedRemote @Inject constructor(
        override val service: LikedService,
) : BaseRemote<LikedService>() {

    fun getAllLiked(postId: Int): Single<List<LikedResponse>> {
        return service.getAllLiked(postId).map(getResponse())
    }

    fun postLiked(postId: Int, username: String, password: String): Single<LikedResponse> {
        return service.postLiked(postId, username, password).map(getResponse())
    }

    fun deleteLiked(postId: Int, username: String, password: String): Single<LikedResponse> {
        return service.deleteLiked(postId, username, password).map(getResponse())
    }
}