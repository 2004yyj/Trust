package kr.hs.dgsw.trust.ui.viewmodel.fragment

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kr.hs.dgsw.domain.request.LoginRequest
import kr.hs.dgsw.domain.usecase.account.PostLoginUseCase
import java.util.concurrent.TimeUnit

class LoginViewModel(private val postLoginUseCase: PostLoginUseCase) : ViewModel() {

    companion object {
        private const val TAG = "LoginViewModel"
    }
    private val compositeDisposable = CompositeDisposable()

    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess = _isSuccess

    private val _isFailure = MutableLiveData<String>()
    val isFailure = _isFailure

    val isLoading = ObservableField<Boolean>()

    val username = MutableLiveData<String>()
    val password = MutableLiveData<String>()

    fun login() {

        isLoading.set(true)

        if (!username.value.isNullOrEmpty() && !password.value.isNullOrEmpty()) {
            val loginRequest = LoginRequest(username.value!!, password.value!!)

            postLoginUseCase.buildUseCaseObservable(PostLoginUseCase.Params(loginRequest))
                    .timeout(15, TimeUnit.SECONDS) {
                        isLoading.set(false)
                        _isFailure.postValue("시간이 초과되었습니다. 연결을 확인하세요.")
                    }
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe ({
                        _isSuccess.postValue(true)
                        isLoading.set(false)
                    }, {
                        isLoading.set(false)
                        _isFailure.postValue(it.message)
                    })
                    .apply {
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