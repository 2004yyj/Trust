package kr.hs.dgsw.trust.ui.viewmodel.fragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kr.hs.dgsw.domain.entity.Post
import kr.hs.dgsw.domain.usecase.post.GetAllPostUseCase

class PostViewModel(private val getAllPostUseCase: GetAllPostUseCase) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()

    val postList = MutableLiveData<ArrayList<Post>>(ArrayList())

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

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}