package kr.hs.dgsw.trust.di.component

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import kr.hs.dgsw.trust.ui.activity.IntroActivity
import kr.hs.dgsw.trust.ui.activity.MainActivity
import kr.hs.dgsw.trust.di.application.MyDaggerApplication
import kr.hs.dgsw.trust.di.module.RemoteModule
import kr.hs.dgsw.trust.di.module.RepositoryModule
import kr.hs.dgsw.trust.di.module.RetrofitModule
import kr.hs.dgsw.trust.di.module.ServiceModule
import kr.hs.dgsw.trust.ui.fragment.LoginFragment
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidInjectionModule::class,
    RetrofitModule::class,
    RepositoryModule::class,
    RemoteModule::class,
    ServiceModule::class
])
interface MyComponent : AndroidInjector<MyDaggerApplication> {

    fun inject(mainActivity: MainActivity)
    fun inject(introActivity: IntroActivity)
    fun inject(loginFragment: LoginFragment)

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: Application) : MyComponent
    }
}