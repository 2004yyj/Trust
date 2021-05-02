package kr.hs.dgsw.trust.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kr.hs.dgsw.trust.R
import kr.hs.dgsw.trust.di.application.MyDaggerApplication
import retrofit2.Retrofit
import javax.inject.Inject

class IntroActivity : AppCompatActivity() {

    @Inject
    lateinit var retrofit: Retrofit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        (application as MyDaggerApplication).daggerComponent.inject(this)
    }
}