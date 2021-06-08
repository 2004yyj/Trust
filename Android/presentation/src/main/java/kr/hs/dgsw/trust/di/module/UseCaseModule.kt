package kr.hs.dgsw.trust.di.module

import dagger.Module
import dagger.Provides
import kr.hs.dgsw.domain.repository.AccountRepository
import kr.hs.dgsw.domain.repository.CommentRepository
import kr.hs.dgsw.domain.repository.LikedRepository
import kr.hs.dgsw.domain.repository.PostRepository
import kr.hs.dgsw.domain.usecase.account.PostLoginUseCase
import kr.hs.dgsw.domain.usecase.account.PostSignUpUseCase
import kr.hs.dgsw.domain.usecase.comment.DeleteCommentUseCase
import kr.hs.dgsw.domain.usecase.comment.GetAllCommentUseCase
import kr.hs.dgsw.domain.usecase.comment.PostCommentUseCase
import kr.hs.dgsw.domain.usecase.comment.UpdateCommentUseCase
import kr.hs.dgsw.domain.usecase.liked.DeleteLikedUseCase
import kr.hs.dgsw.domain.usecase.liked.GetAllLikedUseCase
import kr.hs.dgsw.domain.usecase.liked.PostLikedUseCase
import kr.hs.dgsw.domain.usecase.post.*
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
    fun provideGetAllPostUseCase(repository: PostRepository) =
            GetAllPostUseCase(repository)

    @Provides
    @Singleton
    fun provideGetAllPostByUsernameUseCase(repository: PostRepository) =
            GetAllPostByUsernameUseCase(repository)

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

    @Provides
    @Singleton
    fun provideGetAllLikedUseCase(repository: LikedRepository) =
            GetAllLikedUseCase(repository)

    @Provides
    @Singleton
    fun providePostLikedUseCase(repository: LikedRepository) =
            PostLikedUseCase(repository)

    @Provides
    @Singleton
    fun provideDeleteLikedUseCase(repository: LikedRepository) =
            DeleteLikedUseCase(repository)

    @Provides
    @Singleton
    fun provideGetAllCommentUseCase(repository: CommentRepository) =
            GetAllCommentUseCase(repository)

    @Provides
    @Singleton
    fun providePostCommentUseCase(repository: CommentRepository) =
            PostCommentUseCase(repository)

    @Provides
    @Singleton
    fun provideUpdateCommentUseCase(repository: CommentRepository) =
            UpdateCommentUseCase(repository)

    @Provides
    @Singleton
    fun provideDeleteCommentUseCase(repository: CommentRepository) =
            DeleteCommentUseCase(repository)
}