package kr.hs.dgsw.trust.di.module

import dagger.Module
import dagger.Provides
import kr.hs.dgsw.data.network.remote.AccountRemote
import kr.hs.dgsw.data.network.service.AccountService
import javax.inject.Singleton

@Module
class RemoteModule {
    @Singleton
    @Provides
    fun provideAccountRemote(accountService: AccountService) : AccountRemote =
        AccountRemote(accountService)
}