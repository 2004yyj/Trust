package kr.hs.dgsw.trust.viewmodel.fragment

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {

    val idEditText = MutableLiveData<String>()
    val pwEditText = MutableLiveData<String>()

}