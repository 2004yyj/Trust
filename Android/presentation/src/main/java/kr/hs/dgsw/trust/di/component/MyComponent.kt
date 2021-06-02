package kr.hs.dgsw.trust.di.component

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import kr.hs.dgsw.trust.ui.activity.IntroActivity
import kr.hs.dgsw.trust.ui.activity.MainActivity
import kr.hs.dgsw.trust.di.application.MyDaggerApplication
import kr.hs.dgsw.trust.di.module.RemoteModule
import kr.hs.dgsw.trust.di.module.RepositoryModule
import kr.hs.dgsw.trust.di.module.RetrofitModule
import kr.hs.dgsw.trust.ui.fragment.HomeFragment
import kr.hs.dgsw.trust.ui.fragment.LoginFragment
import kr.hs.dgsw.trust.ui.fragment.SignUpUserInfoFragment
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidSupportInjectionModule::class,
    RetrofitModule::class,
    RepositoryModule::class,
    RemoteModule::class
])
interface MyComponent : AndroidInjector<MyDaggerApplication> {

    fun inject(loginFragment: LoginFragment)
    fun inject(homeFragment: HomeFragment)
    fun inject(signUpUserInfoFragment: SignUpUserInfoFragment)

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: Application) : MyComponent
    }
}