package kr.hs.dgsw.trust.di.module

import dagger.Module
import dagger.Provides
import kr.hs.dgsw.data.network.service.AccountService
import kr.hs.dgsw.data.network.service.PostService
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class ServiceModule {
    @Provides
    @Singleton
    fun provideAccountService(retrofit: Retrofit): AccountService =
        retrofit.create(AccountService::class.java)


    @Provides
    @Singleton
    fun providePostService(retrofit: Retrofit): PostService =
            retrofit.create(PostService::class.java)
}