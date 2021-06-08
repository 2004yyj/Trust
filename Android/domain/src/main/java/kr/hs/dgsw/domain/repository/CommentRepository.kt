package kr.hs.dgsw.domain.repository

import io.reactivex.Single
import kr.hs.dgsw.domain.entity.Comment
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface CommentRepository {

    fun getAllComment(postId: Int) : Single<List<Comment>>

    fun postComment(
            postId: Int,
            username: String,
            password: String,
            content: String,
            isAnonymous: Boolean,
            imageList: List<MultipartBody.Part>?
    ) : Single<List<Comment>>

    fun updateComment(
            commentId: Int,
            username: String,
            password: String,
            content: String?,
            isAnonymous: Boolean?,
            imageList: List<MultipartBody.Part>?
    ) : Single<List<Comment>>

    fun deleteComment(
            commentId: Int,
            username: String,
            password: String,
    ) : Single<List<Comment>>

}