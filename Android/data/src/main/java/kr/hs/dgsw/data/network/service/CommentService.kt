package kr.hs.dgsw.data.network.service

import io.reactivex.Single
import kr.hs.dgsw.data.entity.PostResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface CommentService {

    @GET("/comment/{postId}")
    fun getAllComment() : Single<Response<kr.hs.dgsw.data.util.Response<List<PostResponse>>>>

    @Multipart
    @POST("/comment/{postId}/save")
    fun postComment(
            @Path("postId") postId: Int,
            @Part("username") username: RequestBody,
            @Part("password") password: RequestBody,
            @Part("content") content: RequestBody,
            @Part("isAnonymous") isAnonymous: RequestBody,
            @Part imageList: List<MultipartBody.Part>?
    ) : Single<Response<kr.hs.dgsw.data.util.Response<PostResponse>>>

    @Multipart
    @PUT("/comment/{commentId}/update")
    fun updatePost(
            @Path("commentId") commentId: Int,
            @Part("username") username: RequestBody,
            @Part("password") password: RequestBody,
            @Part("content") content: RequestBody?,
            @Part("isAnonymous") isAnonymous: RequestBody?,
            @Part imageList: List<MultipartBody.Part>?
    ) : Single<Response<kr.hs.dgsw.data.util.Response<PostResponse>>>

    @DELETE("/comment/{commentId}/delete")
    fun deleteComment(
            @Path("commentId") commentId: Int,
            @Query("username") username: String,
            @Query("password") password: String,
    ) : Single<Response<kr.hs.dgsw.data.util.Response<PostResponse>>>
}