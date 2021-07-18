package kr.hs.dgsw.trust.di.component

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import kr.hs.dgsw.trust.di.application.MyDaggerApplication
import kr.hs.dgsw.trust.di.module.NetworkModule
import kr.hs.dgsw.trust.di.module.RemoteModule
import kr.hs.dgsw.trust.di.module.RepositoryModule
import kr.hs.dgsw.trust.ui.activity.IntroActivity
import kr.hs.dgsw.trust.ui.activity.SplashActivity
import kr.hs.dgsw.trust.ui.dialog.CommentFragment
import kr.hs.dgsw.trust.ui.fragment.*
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidSupportInjectionModule::class,
    NetworkModule::class,
    RepositoryModule::class,
    RemoteModule::class
])
interface MyComponent : AndroidInjector<MyDaggerApplication> {

    fun inject(introActivity: IntroActivity)
    fun inject(commentFragment: CommentFragment)
    fun inject(loginFragment: LoginFragment)
    fun inject(homeFragment: HomeFragment)
    fun inject(signUpUserInfoFragment: SignUpUserInfoFragment)
    fun inject(splashActivity: SplashActivity)
    fun inject(addFragment: AddFragment)
    fun inject(updateFragment: UpdateFragment)

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: Application) : MyComponent
    }
}