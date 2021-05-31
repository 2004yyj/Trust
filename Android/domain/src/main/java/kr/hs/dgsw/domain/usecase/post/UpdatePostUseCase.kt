package kr.hs.dgsw.domain.usecase.post

import io.reactivex.Single
import kr.hs.dgsw.domain.entity.Post
import kr.hs.dgsw.domain.repository.PostRepository
import okhttp3.MultipartBody
import javax.inject.Inject

class UpdatePostUseCase @Inject constructor(private val repository: PostRepository) {
    fun updatePost(
            postId: Int,
            username: String,
            password: String,
            content: String?,
            isAnonymous: Boolean?,
            deleteFileList: List<String>,
            updateFileList: List<MultipartBody.Part>?
    ): Single<Post> {
        return repository.updatePost(postId, username, password, isAnonymous, content, deleteFileList, updateFileList)
    }
}