package kr.hs.dgsw.trust.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import kr.hs.dgsw.domain.usecase.account.PostAutoLoginUseCase
import kr.hs.dgsw.trust.R
import kr.hs.dgsw.trust.di.application.MyDaggerApplication
import kr.hs.dgsw.trust.ui.viewmodel.activity.IntroViewModel
import kr.hs.dgsw.trust.ui.viewmodel.factory.IntroViewModelFactory
import javax.inject.Inject

class IntroActivity : AppCompatActivity() {

    @Inject
    lateinit var postAutoLoginUseCase: PostAutoLoginUseCase
    private lateinit var viewModel: IntroViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)
        init()
        observe()
    }

    private fun observe() = with(viewModel) {
        isSuccess.observe(this@IntroActivity) {
            val intent = Intent(this@IntroActivity, MainActivity::class.java)
            startActivity(intent)
        }

        isFailure.observe(this@IntroActivity) {
            Toast.makeText(this@IntroActivity, it, Toast.LENGTH_SHORT).show()
        }
    }

    private fun init() {
        (application as MyDaggerApplication).daggerComponent.inject(this)
        viewModel = ViewModelProvider(this, IntroViewModelFactory(postAutoLoginUseCase))[IntroViewModel::class.java]
        viewModel.postAutoLogin()
    }
}