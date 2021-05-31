package kr.hs.dgsw.trust.ui.viewmodel.fragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kr.hs.dgsw.domain.entity.Comment

class CommentViewModel : ViewModel() {
    val postId = MutableLiveData<Int>()
    val commentList = ArrayList<Comment>()

    fun comment(postId: Int) {

    }
}