package kr.hs.dgsw.trust.di.application

import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import kr.hs.dgsw.trust.di.component.DaggerMyComponent
import kr.hs.dgsw.trust.di.component.MyComponent
import kr.hs.dgsw.trust.ui.util.PreferenceHelper

class MyDaggerApplication : DaggerApplication() {

    lateinit var daggerComponent: MyComponent

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        daggerComponent.inject(this)

        return daggerComponent
    }

    override fun onCreate() {
        daggerComponent = DaggerMyComponent.factory().create(this)
        PreferenceHelper.init(applicationContext)
        super.onCreate()
    }
}