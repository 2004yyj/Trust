package kr.hs.dgsw.trust.ui.viewmodel.fragment

import androidx.databinding.ObservableArrayList
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kr.hs.dgsw.domain.entity.Post
import kr.hs.dgsw.domain.usecase.post.GetPostUseCase

class PostViewModel(private val getPostUseCase: GetPostUseCase) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()
    val postList = ObservableArrayList<Post>()

    fun getAllPost() {
        getPostUseCase.getAllPost()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe ({
                    postList.addAll(it)
                }, {}).apply {
                    compositeDisposable.add(this)
                }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}