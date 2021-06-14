package kr.hs.dgsw.trust.ui.viewmodel.adapter

import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kr.hs.dgsw.domain.usecase.liked.DeleteLikedUseCase
import kr.hs.dgsw.domain.usecase.liked.PostLikedUseCase

class ItemPostViewModel(
        private val postLikedUseCase: PostLikedUseCase,
        private val deleteLikedUseCase: DeleteLikedUseCase
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val id = ObservableField<Int>()
    val name = ObservableField<String>()
    val profileImagePath = ObservableField<String>()
    val content = ObservableField<String>()
    val createdAt = ObservableField<Long>()

    val likedSize = ObservableField<Int>()
    val likedString = ObservableField<String>()

    val imagePathList = ObservableArrayList<String>()

    private val _isFailure = MutableLiveData<String>()
    val isFailure = _isFailure

    fun postLiked(postId: Int, username: String, password: String) {
        postLikedUseCase.buildUseCaseObservable(PostLikedUseCase.Params(postId, username, password))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    likedSize.set(it.size)
                    likedString.set("좋아요 ${likedSize.get()}명")
                }, {
                    _isFailure.value = it.message
                }).apply {
                    compositeDisposable.add(this)
                }

    }

    fun deleteLiked(postId: Int, username: String, password: String) {
        deleteLikedUseCase.buildUseCaseObservable(DeleteLikedUseCase.Params(postId, username, password))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    likedSize.set(it.size)
                    likedString.set("좋아요 ${likedSize.get()}명")
                }, {
                    _isFailure.value = it.message
                }).apply {
                    compositeDisposable.add(this)
                }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}
