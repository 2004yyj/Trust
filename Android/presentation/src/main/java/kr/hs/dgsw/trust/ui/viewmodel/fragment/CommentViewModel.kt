package kr.hs.dgsw.trust.ui.viewmodel.fragment

import androidx.databinding.ObservableArrayList
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kr.hs.dgsw.domain.entity.Comment
import kr.hs.dgsw.domain.usecase.comment.DeleteCommentUseCase
import kr.hs.dgsw.domain.usecase.comment.GetAllCommentUseCase
import kr.hs.dgsw.domain.usecase.comment.PostCommentUseCase
import okhttp3.MultipartBody


class CommentViewModel(
    private val getAllCommentUseCase: GetAllCommentUseCase,
    private val postCommentUseCase: PostCommentUseCase,
    private val deleteCommentUseCase: DeleteCommentUseCase
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    val postId = MutableLiveData<Int>()
    val postCommentText = MutableLiveData<String>()
    val postImageList = ObservableArrayList<MultipartBody.Part>()
    val commentList = MutableLiveData(ArrayList<Comment>())
    private val _isFailure = MutableLiveData<String>()

    fun getAllComment(postId: Int) {
        getAllCommentUseCase.buildUseCaseObservable(GetAllCommentUseCase.Params(postId))
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                commentList.value = it as ArrayList<Comment>?
            }, {
                _isFailure.value = it.message
            }).apply {
                compositeDisposable.add(this)
            }
    }

    fun postComment(content: String) {
        if (postId.value != null) {

            val list = if(postImageList.isNotEmpty()) {
                val builder = MultipartBody.Builder().setType(MultipartBody.FORM)
                postImageList.forEach {
                    builder.addPart(it)
                }
                builder.build().parts
            } else {
                null
            }

            postCommentUseCase.buildUseCaseObservable(
                PostCommentUseCase.Params(postId.value!!, content, list)
            )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    commentList.value = it as ArrayList<Comment>?
                }, {
                    _isFailure.value = it.message
                }).apply {
                    compositeDisposable.add(this)
                }
        }

    }

    fun deleteComment(commentId: Int) {
        deleteCommentUseCase.buildUseCaseObservable(DeleteCommentUseCase.Params(commentId))
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                commentList.value = it as ArrayList<Comment>?
            }, {
                _isFailure.value = it.message
            }).apply {
                compositeDisposable.add(this)
            }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}