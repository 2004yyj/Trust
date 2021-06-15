package kr.hs.dgsw.domain.repository

import io.reactivex.Single
import kr.hs.dgsw.domain.entity.Comment
import okhttp3.MultipartBody

interface CommentRepository {

    fun getAllComment(postId: Int) : Single<List<Comment>>

    fun postComment(
            postId: Int,
            content: String,
            imageList: List<MultipartBody.Part>?
    ) : Single<List<Comment>>

    fun updateComment(
            commentId: Int,
            content: String?,
            imageList: List<MultipartBody.Part>?
    ) : Single<List<Comment>>

    fun deleteComment(
            commentId: Int,
    ) : Single<List<Comment>>

}