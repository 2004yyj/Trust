package kr.hs.dgsw.trust.ui.util.binding

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("setTime")
fun TextView.setTime(time: Long) {
    val sdf = SimpleDateFormat("yyyy년 MM월 dd일", Locale.KOREA)
    this.text = sdf.format(Date(time))
}