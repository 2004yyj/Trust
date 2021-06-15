package kr.hs.dgsw.trust.ui.viewmodel.fragment

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kr.hs.dgsw.domain.entity.Comment
import kr.hs.dgsw.domain.usecase.comment.GetAllCommentUseCase

class CommentViewModel(private val getAllCommentUseCase: GetAllCommentUseCase) : ViewModel() {
    private val compositeDisposable =  CompositeDisposable()
    val postId = MutableLiveData<Int>()
    val commentList = MutableLiveData(ArrayList<Comment>())
    private val _isFailure = MutableLiveData<String>()

    fun getAllComment(postId: Int) {
        getAllCommentUseCase.buildUseCaseObservable(GetAllCommentUseCase.Params(postId))
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                commentList.value = it as ArrayList<Comment>?
            }, {
                Log.d("asfasdaf", "getAllComment: ${it.message}")
                _isFailure.value = it.message
            }).apply {
                compositeDisposable.add(this)
            }
    }

    fun postComment(
        content: String,
        imageList: List<MultipartBody.Part>?
    ) {
        if (postId.value != null) {
            postCommentUseCase.buildUseCaseObservable(
                PostCommentUseCase.Params(postId.value!!, content, imageList)
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

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}