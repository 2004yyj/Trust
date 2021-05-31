package kr.hs.dgsw.trust.di.module

import dagger.Module
import dagger.Provides
import kr.hs.dgsw.domain.repository.AccountRepository
import kr.hs.dgsw.domain.repository.PostRepository
import kr.hs.dgsw.domain.usecase.account.PostLoginUseCase
import kr.hs.dgsw.domain.usecase.account.PostSignUpUseCase
import kr.hs.dgsw.domain.usecase.post.DeletePostUseCase
import kr.hs.dgsw.domain.usecase.post.GetPostUseCase
import kr.hs.dgsw.domain.usecase.post.PostPostUseCase
import kr.hs.dgsw.domain.usecase.post.UpdatePostUseCase
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

    @Provides
    @Singleton
    fun provideGetPostUseCase(repository: PostRepository) =
            GetPostUseCase(repository)

    @Provides
    @Singleton
    fun providePostPostUseCase(repository: PostRepository) =
            PostPostUseCase(repository)

    @Provides
    @Singleton
    fun provideUpdatePostUseCase(repository: PostRepository) =
            UpdatePostUseCase(repository)

    @Provides
    @Singleton
    fun provideDeletePostUseCase(repository: PostRepository) =
            DeletePostUseCase(repository)
}