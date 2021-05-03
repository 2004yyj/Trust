package kr.hs.dgsw.trust.di.component

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import kr.hs.dgsw.trust.ui.activity.IntroActivity
import kr.hs.dgsw.trust.ui.activity.MainActivity
import kr.hs.dgsw.trust.di.application.MyDaggerApplication
import kr.hs.dgsw.trust.di.module.RetrofitModule
import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidInjectionModule::class, RetrofitModule::class])
interface MyComponent : AndroidInjector<MyDaggerApplication> {

    fun inject(mainActivity: MainActivity)
    fun inject(introActivity: IntroActivity)

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: Application) : MyComponent
    }
}