package kr.hs.dgsw.trust.di.module

import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import kr.hs.dgsw.data.util.ServerAddress.ADDR
import kr.hs.dgsw.trust.interceptor.AuthInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class RetrofitModule {

    @Provides
    @Singleton
    fun proviceInterceptor() = AuthInterceptor()

    @Provides
    @Singleton
    fun provideClient(interceptor: AuthInterceptor) =
        OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

    @Provides
    @Singleton
    fun proviceGson() = Gson()

    @Provides
    @Singleton
    fun provideRetrofit(gson: Gson, client: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .client(client)
            .baseUrl(ADDR)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
}