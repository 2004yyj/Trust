package kr.hs.dgsw.domain.usecase.post

import io.reactivex.Single
import kr.hs.dgsw.domain.base.ParamsUseCase
import kr.hs.dgsw.domain.entity.Post
import kr.hs.dgsw.domain.repository.PostRepository
import okhttp3.MultipartBody
import javax.inject.Inject

class UpdatePostUseCase @Inject constructor(
        private val repository: PostRepository
): ParamsUseCase<UpdatePostUseCase.Params, Single<Post>>() {
    override fun buildUseCaseObservable(params: Params): Single<Post> {
        return repository.updatePost(
                params.postId,
                params.username,
                params.password,
                params.isAnonymous,
                params.content,
                params.deleteFileList,
                params.updateFileList
        )
    }

    data class Params(
            val postId: Int,
            val username: String,
            val password: String,
            val content: String?,
            val isAnonymous: Boolean?,
            val deleteFileList: List<String>,
            val updateFileList: List<MultipartBody.Part>?
    )
}