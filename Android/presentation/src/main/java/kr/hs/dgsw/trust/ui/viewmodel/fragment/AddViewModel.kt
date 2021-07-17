package kr.hs.dgsw.trust.ui.viewmodel.fragment

import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kr.hs.dgsw.domain.usecase.post.PostPostUseCase
import kr.hs.dgsw.trust.ui.util.SingleLiveEvent
import okhttp3.MultipartBody

class AddViewModel(
    private val postPostUseCase: PostPostUseCase
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val content = ObservableField<String>()
    val isAnonymous = ObservableField(false)

    private val imageList = ArrayList<MultipartBody.Part>()

    val isLoading = ObservableField<Boolean>()

    private val _isFailure = MutableLiveData<String>()
    val isFailure: LiveData<String> = _isFailure

    private val _isSuccess = SingleLiveEvent<Unit>()
    val isSuccess: LiveData<Unit> = _isSuccess

    fun addImageList(list: List<MultipartBody.Part>) {
        imageList.clear()
        imageList.addAll(list)
    }

    fun postPost() {
        Log.d("AddViewModel", "postPost: ")

        isLoading.set(true)

        val contentValue = content.get()
        val isAnonymousValue = isAnonymous.get()!!

        if (contentValue != null && contentValue.isNotBlank()) {
            postPostUseCase.buildUseCaseObservable(
                PostPostUseCase.Params(
                    contentValue,
                    isAnonymousValue,
                    imageList
                )
            )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    _isSuccess.call()
                    isLoading.set(false)
                }, {
                    _isFailure.postValue(it.message)
                    isLoading.set(false)
                }).apply {
                    compositeDisposable.add(this)
                }
            return
        }

        _isFailure.postValue("아이디 또는 비밀번호를 입력해 주세요.")
        isLoading.set(false)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}