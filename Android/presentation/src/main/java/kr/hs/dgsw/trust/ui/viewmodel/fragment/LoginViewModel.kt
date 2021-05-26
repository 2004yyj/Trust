package kr.hs.dgsw.trust.ui.viewmodel.fragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kr.hs.dgsw.domain.usecase.account.PostLoginUseCase

class LoginViewModel(private val postLoginUseCase: PostLoginUseCase) : ViewModel() {

    companion object {
        private const val TAG = "LoginViewModel"
    }
    private val compositeDisposable = CompositeDisposable()

    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess = _isSuccess

    private val _isFailure = MutableLiveData<String>()
    val isFailure = _isFailure

    val username = MutableLiveData<String>()
    val password = MutableLiveData<String>()

    fun login() {
        postLoginUseCase.postLogin(username.value!!, password.value!!)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe ({
                _isSuccess.postValue(true)
            }, {
                _isFailure.postValue(it.message)
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