package kr.hs.dgsw.trust.viewmodel.fragment

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SignUpViewModel : ViewModel() {

    val id = MutableLiveData<String>()
    val name = MutableLiveData<String>()
    val pw = MutableLiveData<String>()
    val pwChk = MutableLiveData<String>()

    fun signUp() {
        //TODO 서버 측에서 아이디 비밀번호 확인 후 회원가입 처리
    }

}