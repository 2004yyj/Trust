package kr.hs.dgsw.data.datasource

import io.reactivex.Single
import kr.hs.dgsw.data.base.NoCacheDataSource
import kr.hs.dgsw.data.mapper.toEntity
import kr.hs.dgsw.data.network.remote.CommentRemote
import kr.hs.dgsw.domain.entity.Comment
import okhttp3.MultipartBody
import javax.inject.Inject

class CommentDataSource @Inject constructor(
        override val remote: CommentRemote,
): NoCacheDataSource<CommentRemote>() {

    fun getAllComment(postId: Int): Single<List<Comment>> {
        return remote.getAllComment(postId).map { commentResponseList ->
            val commentList = ArrayList<Comment>()
            commentResponseList.forEach {
                commentList.add(it.toEntity())
            }
            commentList
        }
    }

    fun postComment(
            postId: Int,
            content: String,
            imageList: List<MultipartBody.Part>?
    ): Single<List<Comment>> {
        return remote.postComment(
                postId,
                content,
                imageList
        ).map { commentResponseList ->
            val commentList = ArrayList<Comment>()
            commentResponseList.forEach {
                commentList.add(it.toEntity())
            }
            commentList
        }
    }

    fun updateComment(
            commentId: Int,
            content: String?,
            imageList: List<MultipartBody.Part>?
    ): Single<List<Comment>> {
        return remote.updateComment(
                commentId,
                content,
                imageList
        ).map { commentResponseList ->
            val commentList = ArrayList<Comment>()
            commentResponseList.forEach {
                commentList.add(it.toEntity())
            }
            commentList
        }
    }

    fun deleteComment(commentId: Int): Single<List<Comment>> {
        return remote.deleteComment(commentId).map { commentResponseList ->
            val commentList = ArrayList<Comment>()
            commentResponseList.forEach {
                commentList.add(it.toEntity())
            }
            commentList
        }
    }

}