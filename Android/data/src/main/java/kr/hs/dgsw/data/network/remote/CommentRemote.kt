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
            content: String,
            imageList: List<MultipartBody.Part>?
    ): Single<List<CommentResponse>> {
        val textType = "text/plain".toMediaType()
        val contentBody = content.toRequestBody(textType)

        return service.postComment(postId, contentBody, imageList).map(getResponse())
    }

    fun updateComment(
            commentId: Int,
            content: String?,
            imageList: List<MultipartBody.Part>?
    ): Single<List<CommentResponse>> {
        val textType = "text/plain".toMediaType()
        val contentBody = content?.toRequestBody(textType)

        return service.updateComment(commentId, contentBody, imageList).map(getResponse())
    }

    fun deleteComment(commentId: Int): Single<List<CommentResponse>> {
        return service.deleteComment(commentId).map(getResponse())
    }
}