package kr.hs.dgsw.trust.ui.viewmodel.fragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kr.hs.dgsw.domain.entity.Post
import kr.hs.dgsw.domain.usecase.post.DeletePostUseCase
import kr.hs.dgsw.domain.usecase.post.GetAllPostUseCase
import kr.hs.dgsw.domain.usecase.post.GetPostUseCase

class PostViewModel(
    private val getAllPostUseCase: GetAllPostUseCase,
    private val deletePostUseCase: DeletePostUseCase,
    private val getPostUseCase: GetPostUseCase
) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()

    val postList = MutableLiveData(ArrayList<Post>())
    val post = MutableLiveData<Post>()

    private val _isFailure = MutableLiveData<String>()
    val isFailure = _isFailure

    fun getAllPost() {
        getAllPostUseCase.buildUseCaseObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    postList.postValue(it as ArrayList<Post>?)
                }, {
                    _isFailure.postValue(it.message)
                })
                .apply {
                    compositeDisposable.add(this)
                }
    }

    fun deletePost(postId: Int) {
        deletePostUseCase.buildUseCaseObservable(DeletePostUseCase.Params(postId))
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                postList.postValue(it as ArrayList<Post>?)
            } ,{
                _isFailure.postValue(it.message)
            }).apply {
                compositeDisposable.add(this)
            }
    }

    fun getPost(postId: Int) {
        getPostUseCase.buildUseCaseObservable(GetPostUseCase.Params(postId))
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                post.postValue(it)
            } ,{
                _isFailure.postValue(it.message)
            }).apply {
                compositeDisposable.add(this)
            }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}