package kr.hs.dgsw.trust.ui.viewmodel.fragment

import android.net.Uri
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kr.hs.dgsw.domain.usecase.post.UpdatePostUseCase
import kr.hs.dgsw.trust.ui.util.SingleLiveEvent
import okhttp3.MultipartBody

class UpdateViewModel(private val updatePostUseCase: UpdatePostUseCase): ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    var postId = 0

    val content = ObservableField<String>()
    val isAnonymous = ObservableField(false)

    val updateFileList = ArrayList<Uri>()
    val deleteFileList = ArrayList<String>()

    val isLoading = ObservableField<Boolean>()

    private val _isFailure = MutableLiveData<String>()
    val isFailure: LiveData<String> = _isFailure

    private val _isSuccess = SingleLiveEvent<Unit>()
    val isSuccess: LiveData<Unit> = _isSuccess

    fun updatePost(multipartList: List<MultipartBody.Part>?) {

        val contentValue = content.get()
        val isAnonymousValue = isAnonymous.get()!!

        if (contentValue != null && contentValue.isNotBlank()) {
            updatePostUseCase.buildUseCaseObservable(
                UpdatePostUseCase.Params(
                    postId,
                    contentValue,
                    isAnonymousValue,
                    deleteFileList,
                    multipartList
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

        _isFailure.postValue("빈 칸이 없는지 확인해 주세요.")
        isLoading.set(false)
    }
}
