package kr.hs.dgsw.trust.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kr.hs.dgsw.trust.R
import kr.hs.dgsw.trust.di.application.MyDaggerApplication
import retrofit2.Retrofit
import javax.inject.Inject

class IntroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)
    }
}