package kr.hs.dgsw.trust.di.module

import dagger.Module
import dagger.Provides
import kr.hs.dgsw.data.datasource.AccountDataSource
import kr.hs.dgsw.data.datasource.PostDataSource
import kr.hs.dgsw.data.network.remote.AccountRemote
import kr.hs.dgsw.data.network.remote.PostRemote
import javax.inject.Singleton

@Module
class DataSourceModule {
    @Singleton
    @Provides
    fun provideAccountDataSource(accountRemote: AccountRemote) : AccountDataSource =
            AccountDataSource(accountRemote)


    @Singleton
    @Provides
    fun providePostDataSource(postRemote: PostRemote) : PostDataSource =
            PostDataSource(postRemote)
}