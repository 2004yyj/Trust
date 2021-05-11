package kr.hs.dgsw.trust.di.module

import dagger.Module
import dagger.Provides
import kr.hs.dgsw.domain.repository.AccountRepository
import kr.hs.dgsw.domain.usecase.account.PostLoginUseCase
import kr.hs.dgsw.domain.usecase.account.PostSignUpUseCase
import javax.inject.Singleton

@Module
class UseCaseModule {
    @Provides
    @Singleton
    fun provideLoginUseCase(repository: AccountRepository) =
        PostLoginUseCase(repository)

    @Provides
    @Singleton
    fun provideSignUpUseCase(repository: AccountRepository) =
        PostSignUpUseCase(repository)
}