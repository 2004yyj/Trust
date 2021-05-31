package kr.hs.dgsw.data.network.service

import io.reactivex.Single
import kr.hs.dgsw.data.entity.PostResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface LikedService {

    @GET("/liked/{postId}")
    fun getAllComment() : Single<Response<kr.hs.dgsw.data.util.Response<List<PostResponse>>>>

    @Multipart
    @POST("/liked/{postId}/save")
    fun postComment(
            @Path("postId") postId: Int,
            @Part("username") username: RequestBody,
            @Part("password") password: RequestBody,
            @Part("content") content: RequestBody,
            @Part("isAnonymous") isAnonymous: RequestBody,
            @Part imageList: List<MultipartBody.Part>?
    ) : Single<Response<kr.hs.dgsw.data.util.Response<PostResponse>>>

    @Multipart
    @PUT("/liked/{likedId}/update")
    fun updatePost(
            @Path("likedId") commentId: Int,
            @Part("username") username: RequestBody,
            @Part("password") password: RequestBody,
            @Part("content") content: RequestBody?,
            @Part("isAnonymous") isAnonymous: RequestBody?,
            @Part imageList: List<MultipartBody.Part>?
    ) : Single<Response<kr.hs.dgsw.data.util.Response<PostResponse>>>

    @DELETE("/liked/{likedId}/delete")
    fun deleteComment(
            @Path("likedId") commentId: Int,
            @Query("username") username: String,
            @Query("password") password: String,
    ) : Single<Response<kr.hs.dgsw.data.util.Response<PostResponse>>>
}