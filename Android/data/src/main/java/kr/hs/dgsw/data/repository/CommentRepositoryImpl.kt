package kr.hs.dgsw.data.repository

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

    override fun postComment(postId: Int, content: String, imageList: List<MultipartBody.Part>?): Single<List<Comment>> {
        return commentDataSource.postComment(postId, content, imageList)
    }

    override fun updateComment(commentId: Int, content: String?, imageList: List<MultipartBody.Part>?): Single<List<Comment>> {
        return commentDataSource.updateComment(commentId, content, imageList)
    }

    override fun deleteComment(commentId: Int): Single<List<Comment>> {
        return commentDataSource.deleteComment(commentId)
    }
}