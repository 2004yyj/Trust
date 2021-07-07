package kr.hs.dgsw.trust.di.module

import dagger.Module
import dagger.Provides
import kr.hs.dgsw.data.network.remote.AccountRemote
import kr.hs.dgsw.data.network.remote.CommentRemote
import kr.hs.dgsw.data.network.remote.LikedRemote
import kr.hs.dgsw.data.network.remote.PostRemote
import kr.hs.dgsw.data.network.service.AccountService
import kr.hs.dgsw.data.network.service.CommentService
import kr.hs.dgsw.data.network.service.LikedService
import kr.hs.dgsw.data.network.service.PostService
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class RemoteModule {
    @Provides
    @Singleton
    fun provideAccountRemote(retrofit: Retrofit) : AccountRemote =
            AccountRemote(retrofit.create(AccountService::class.java))

    @Provides
    @Singleton
    fun providePostRemote(retrofit: Retrofit) : PostRemote =
            PostRemote(retrofit.create(PostService::class.java))

    @Provides
    @Singleton
    fun provideLikedRemote(retrofit: Retrofit) : LikedRemote =
            LikedRemote(retrofit.create(LikedService::class.java))

    @Provides
    @Singleton
    fun provideCommentRemote(retrofit: Retrofit) : CommentRemote =
            CommentRemote(retrofit.create(CommentService::class.java))
}