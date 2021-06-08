package kr.hs.dgsw.data.network.remote

import io.reactivex.Single
import kr.hs.dgsw.data.base.BaseRemote
import kr.hs.dgsw.data.entity.CommentResponse
import kr.hs.dgsw.data.network.service.CommentService
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class CommentRemote @Inject constructor(
        override val service: CommentService
) : BaseRemote<CommentService>() {

    fun getAllComment(postId: Int): Single<List<CommentResponse>> {
        return service.getAllComment(postId).map(getResponse())
    }

    fun postComment(
            postId: Int,
            username: String,
            password: String,
            content: String,
            isAnonymous: Boolean,
            imageList: List<MultipartBody.Part>?
    ): Single<List<CommentResponse>> {
        val textType = "text/plain".toMediaType()
        val usernameBody = username.toRequestBody(textType)
        val passwordBody = password.toRequestBody(textType)
        val contentBody = content.toRequestBody(textType)

        return service.postComment(postId, usernameBody, passwordBody, contentBody, isAnonymous, imageList).map(getResponse())
    }

    fun updateComment(
            commentId: Int,
            username: String,
            password: String,
            content: String?,
            isAnonymous: Boolean?,
            imageList: List<MultipartBody.Part>?
    ): Single<List<CommentResponse>> {
        val textType = "text/plain".toMediaType()
        val usernameBody = username.toRequestBody(textType)
        val passwordBody = password.toRequestBody(textType)
        val contentBody = content?.toRequestBody(textType)

        return service.updateComment(commentId, usernameBody, passwordBody, contentBody, isAnonymous, imageList).map(getResponse())
    }

    fun deleteComment(
            commentId: Int,
            username: String,
            password: String
    ): Single<List<CommentResponse>> {
        return service.deleteComment(commentId, username, password).map(getResponse())
    }
}