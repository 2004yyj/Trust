package kr.hs.dgsw.trust.ui.viewmodel.adapter

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ItemPostViewModel : ViewModel() {
    val id = MutableLiveData<Int>()
    val name = MutableLiveData<String>()
    val profileImagePath = MutableLiveData<String>()
    val content = MutableLiveData<String>()
    val createdAt = MutableLiveData<Long>()
    val isChecked = MutableLiveData<Boolean>()
}
