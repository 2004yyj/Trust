package kr.hs.dgsw.trust.viewmodel.fragment

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {

    val id = MutableLiveData<String>()
    val pw = MutableLiveData<String>()

    fun login() {
        //TODO 서버 측에서 아이디 비밀번호 확인 후 로그인 처리
    }

}