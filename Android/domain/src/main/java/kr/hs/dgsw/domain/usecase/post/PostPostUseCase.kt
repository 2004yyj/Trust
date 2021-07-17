package kr.hs.dgsw.domain.usecase.post

import android.util.Log
import io.reactivex.Single
import kr.hs.dgsw.domain.base.ParamsUseCase
import kr.hs.dgsw.domain.entity.Post
import kr.hs.dgsw.domain.repository.PostRepository
import okhttp3.MultipartBody
import javax.inject.Inject

class PostPostUseCase @Inject constructor(
        private val repository: PostRepository
): ParamsUseCase<PostPostUseCase.Params, Single<Post>>() {

    override fun buildUseCaseObservable(params: Params): Single<Post> {
        Log.d("PostPostUseCase", "buildUseCaseObservable: ${params.content}")

        return repository.postPost(params.isAnonymous, params.content, params.imageList)
    }

    data class Params(
            val content: String,
            val isAnonymous: Boolean,
            val imageList: List<MultipartBody.Part>?
    )
}