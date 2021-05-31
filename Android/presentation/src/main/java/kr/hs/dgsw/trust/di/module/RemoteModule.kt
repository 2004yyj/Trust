package kr.hs.dgsw.trust.di.module

import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import kr.hs.dgsw.data.network.remote.AccountRemote
import kr.hs.dgsw.data.network.remote.PostRemote
import kr.hs.dgsw.data.network.service.AccountService
import kr.hs.dgsw.data.network.service.PostService
import javax.inject.Singleton

@Module
class RemoteModule {
    @Provides
    @Singleton
    fun provideAccountRemote(accountService: AccountService, gson: Gson) : AccountRemote =
        AccountRemote(accountService, gson)


    @Provides
    @Singleton
    fun providePostRemote(postService: PostService, gson: Gson) : PostRemote =
            PostRemote(postService, gson)
}