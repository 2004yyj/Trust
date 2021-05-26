package kr.hs.dgsw.trust.ui.viewmodel.fragment

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kr.hs.dgsw.domain.usecase.account.PostSignUpUseCase
import okhttp3.MultipartBody

class SignUpViewModel(private val postSignUpUseCase: PostSignUpUseCase) : ViewModel() {

    val username = MutableLiveData<String>()
    val name = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    val passwordChk = MutableLiveData<String>()

    private val compositeDisposable = CompositeDisposable()

    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess = _isSuccess

    private val _isFailure = MutableLiveData<String>()
    val isFailure = _isFailure

    fun signUp(profileImage: MultipartBody.Part?) {
        postSignUpUseCase.postSignUp(name.value!!, username.value!!, password.value!!, profileImage)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                _isSuccess.value = true
            }, {
                _isFailure.value = it.message
            })
            .apply {
                compositeDisposable.add(this)
            }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

}