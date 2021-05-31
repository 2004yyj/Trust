package kr.hs.dgsw.trust.ui.util.binding

import android.util.Log
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kr.hs.dgsw.domain.entity.Post
import kr.hs.dgsw.trust.ui.adapter.PostAdapter
import java.text.SimpleDateFormat
import java.util.*

object HomeBindingAdapter {
    @JvmStatic
    @BindingAdapter("setTime")
    fun setTime(textView: TextView, time: Long) {
        val sdf = SimpleDateFormat("yyyy년 MM월 dd일", Locale.KOREA)
        textView.text = sdf.format(Date(time))
    }
}