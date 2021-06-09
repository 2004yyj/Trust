package kr.hs.dgsw.data.repository

import android.util.Log
import io.reactivex.Single
import kr.hs.dgsw.data.datasource.CommentDataSource
import kr.hs.dgsw.domain.entity.Comment
import kr.hs.dgsw.domain.repository.CommentRepository
import okhttp3.MultipartBody
import javax.inject.Inject

class CommentRepositoryImpl @Inject constructor(
        private val commentDataSource: CommentDataSource
) : CommentRepository {
    override fun getAllComment(postId: Int): Single<List<Comment>> {
        return commentDataSource.getAllComment(postId)
    }

    override fun postComment(postId: Int, username: String, password: String, content: String, isAnonymous: Boolean, imageList: List<MultipartBody.Part>?): Single<List<Comment>> {
        return commentDataSource.postComment(postId, username, password, content, isAnonymous, imageList)
    }

    override fun updateComment(commentId: Int, username: String, password: String, content: String?, isAnonymous: Boolean?, imageList: List<MultipartBody.Part>?): Single<List<Comment>> {
        return commentDataSource.updateComment(commentId, username, password, content, isAnonymous, imageList)
    }

    override fun deleteComment(commentId: Int, username: String, password: String): Single<List<Comment>> {
        return commentDataSource.deleteComment(commentId, username, password)
    }
}