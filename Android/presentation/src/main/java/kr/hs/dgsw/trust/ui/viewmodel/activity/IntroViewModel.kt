package kr.hs.dgsw.trust.ui.viewmodel.activity

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kr.hs.dgsw.domain.entity.Token
import kr.hs.dgsw.domain.usecase.account.PostAutoLoginUseCase

class IntroViewModel(private val postAutoLoginUseCase: PostAutoLoginUseCase) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    private val _isSuccess = MutableLiveData<Token>()
    val isSuccess = _isSuccess
    private val _isFailure = MutableLiveData<String>()
    val isFailure = _isFailure

    fun postAutoLogin() {
        postAutoLoginUseCase.buildUseCaseObservable()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                _isSuccess.postValue(it)
            }, {
                _isFailure.postValue(it.message)
            }).apply {
                compositeDisposable.add(this)
            }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

}