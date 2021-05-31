package kr.hs.dgsw.trust.di.module

import dagger.Module
import dagger.Provides
import kr.hs.dgsw.data.datasource.AccountDataSource
import kr.hs.dgsw.data.datasource.PostDataSource
import kr.hs.dgsw.data.network.remote.AccountRemote
import kr.hs.dgsw.data.network.service.AccountService
import kr.hs.dgsw.data.repository.AccountRepositoryImpl
import kr.hs.dgsw.data.repository.PostRepositoryImpl
import kr.hs.dgsw.domain.repository.AccountRepository
import kr.hs.dgsw.domain.repository.PostRepository
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Singleton
    @Provides
    fun provideAccountRepository(accountDataSource: AccountDataSource) : AccountRepository =
        AccountRepositoryImpl(accountDataSource)

    @Singleton
    @Provides
    fun providePostRepository(postDataSource: PostDataSource) : PostRepository =
            PostRepositoryImpl(postDataSource)
}