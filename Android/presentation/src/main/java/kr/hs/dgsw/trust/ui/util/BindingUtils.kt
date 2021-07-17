package kr.hs.dgsw.trust.ui.util

import android.net.Uri
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import kr.hs.dgsw.data.util.ServerAddress.ADDR_IMG
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("setTime")
fun TextView.setTime(time: Long) {
    val sdf = SimpleDateFormat("yyyy년 MM월 dd일", Locale.KOREA)
    this.text = sdf.format(Date(time))
}

@BindingAdapter("setImage")
fun ImageView.setImage(imagePath: String) {
    Glide.with(this.context).load(ADDR_IMG+imagePath).centerCrop().into(this)
}

@BindingAdapter("setImage")
fun ImageView.setImage(imageUri: Uri) {
    Glide.with(this.context).load(imageUri).centerCrop().into(this)
}

fun MotionLayout.setOnTransitionCompletedListener(listener: (MotionLayout?, Int) -> Unit) {
    this.setTransitionListener(object : MotionLayout.TransitionListener {
        override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
            listener(motionLayout, currentId)
        }
        override fun onTransitionChange(motionLayout: MotionLayout?, startId: Int, endId: Int, progress: Float) {

        }
        override fun onTransitionStarted(motionLayout: MotionLayout?, startId: Int, endId: Int) {
        }
        override fun onTransitionTrigger(motionLayout: MotionLayout?, triggerId: Int, positive: Boolean, progress: Float) {

        }
    })
}