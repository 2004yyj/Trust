package kr.hs.dgsw.trust.di.module

import dagger.Module
import dagger.Provides
import kr.hs.dgsw.domain.repository.AccountRepository
import kr.hs.dgsw.domain.usecase.account.PostLoginUseCase
import kr.hs.dgsw.domain.usecase.account.PostSignInUseCase
import javax.inject.Singleton

@Module
class UseCaseModule {
    @Provides
    @Singleton
    fun provideLoginUseCase(repository: AccountRepository) =
        PostLoginUseCase(repository)

    @Provides
    @Singleton
    fun provideSignInUseCase(repository: AccountRepository) =
        PostSignInUseCase(repository)
}