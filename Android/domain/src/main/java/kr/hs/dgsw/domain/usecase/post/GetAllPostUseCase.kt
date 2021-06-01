package kr.hs.dgsw.domain.usecase.post

import io.reactivex.Single
import kr.hs.dgsw.domain.base.BaseUseCase
import kr.hs.dgsw.domain.entity.Post
import kr.hs.dgsw.domain.repository.PostRepository
import okhttp3.MultipartBody
import javax.inject.Inject

class GetAllPostUseCase @Inject constructor(
        private val repository: PostRepository
): BaseUseCase<Single<List<Post>>>() {

    override fun buildUseCaseObservable(): Single<List<Post>> {
        return repository.getAllPost()
    }

}