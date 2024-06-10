package com.changs.routesearch.di

import com.changs.routesearch.BuildConfig
import com.changs.routesearch.util.Constant.CONNECT_TIME_OUT
import com.changs.routesearch.util.Constant.READ_TIME_OUT
import com.changs.routesearch.util.Constant.TMAP_BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {
    @Singleton
    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
    }

    @Singleton
    @Provides
    fun provideRequestInterceptor(): Interceptor = Interceptor { chain ->
        val builder: Request.Builder = chain.request().newBuilder()

        builder.addHeader(
            "appKey", (BuildConfig.TMAP_APP_KEY)
        )

        chain.proceed(builder.build())
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor, requestInterceptor: Interceptor
    ): OkHttpClient = OkHttpClient.Builder().readTimeout(READ_TIME_OUT, TimeUnit.MILLISECONDS)
        .connectTimeout(CONNECT_TIME_OUT, TimeUnit.MILLISECONDS)
        .addInterceptor(httpLoggingInterceptor).addInterceptor(requestInterceptor).build()

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder().baseUrl(TMAP_BASE_URL).client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create()).build()
    }
}