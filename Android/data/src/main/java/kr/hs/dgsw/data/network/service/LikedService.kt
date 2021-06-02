package kr.hs.dgsw.data.network.service

import io.reactivex.Single
import kr.hs.dgsw.data.entity.LikedResponse
import retrofit2.Response
import retrofit2.http.*

interface LikedService {

    @GET("/liked")
    fun getAllLiked(
            @Query("postId") postId: Int
    ) : Single<Response<kr.hs.dgsw.data.util.Response<List<LikedResponse>>>>

    @POST("/liked/save")
    fun postLiked(
            @Query("postId") postId: Int,
            @Query("username") username: String,
            @Query("password") password: String
    ) : Single<Response<kr.hs.dgsw.data.util.Response<LikedResponse>>>

    @DELETE("/liked/delete")
    fun deleteLiked(
            @Query("likedId") commentId: Int,
            @Query("username") username: String,
            @Query("password") password: String,
    ) : Single<Response<kr.hs.dgsw.data.util.Response<LikedResponse>>>
}