package kr.hs.dgsw.trust.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import kr.hs.dgsw.domain.usecase.account.PostAutoLoginUseCase
import kr.hs.dgsw.trust.R
import kr.hs.dgsw.trust.di.application.MyDaggerApplication
import kr.hs.dgsw.trust.ui.util.PreferenceHelper.autoLogin
import kr.hs.dgsw.trust.ui.util.PreferenceHelper.token
import kr.hs.dgsw.trust.ui.viewmodel.activity.SplashViewModel
import kr.hs.dgsw.trust.ui.viewmodel.factory.SplashViewModelFactory
import javax.inject.Inject

class SplashActivity : AppCompatActivity() {

    @Inject
    lateinit var postAutoLoginUseCase: PostAutoLoginUseCase
    private lateinit var viewModel: SplashViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        init()
        observe()

        if (token != null && autoLogin) {
            viewModel.postAutoLogin()
        } else {
            val intent = Intent(this@SplashActivity, IntroActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun observe() = with(viewModel) {
        isSuccess.observe(this@SplashActivity) {
            val intent = Intent(this@SplashActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        isFailure.observe(this@SplashActivity) {
            Toast.makeText(this@SplashActivity, "세션이 만료되었습니다. 다시 로그인 해 주세요.", Toast.LENGTH_SHORT).show()
            val intent = Intent(this@SplashActivity, IntroActivity::class.java)
            startActivity(intent)
            finish()
        }
        isLoading.observe(this@SplashActivity) {
            if (it) {

            } else {

            }
        }
    }

    private fun init() {
        (application as MyDaggerApplication).daggerComponent.inject(this)
        viewModel = ViewModelProvider(this, SplashViewModelFactory(postAutoLoginUseCase))[SplashViewModel::class.java]
        viewModel.postAutoLogin()
    }

}