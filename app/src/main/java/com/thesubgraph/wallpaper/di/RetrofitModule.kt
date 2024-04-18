package com.thesubgraph.wallpaper.di

import android.content.Context
import com.google.gson.Gson
import com.thesubgraph.wallpaper.data.common.RequestWrapper
import com.thesubgraph.wallpaper.data.common.WebServiceInterface
import com.thesubgraph.wallpaper.data.remote.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {
    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Singleton
    @Provides
    fun provideGsonConverterFactory(): GsonConverterFactory =
        GsonConverterFactory.create()

    @Singleton
    @Provides
    fun provideGson(): Gson {
        return Gson()
    }

    @Singleton
    @Provides
    fun providerRequestWrapper(
        gson: Gson,
        @ApplicationContext context: Context,
    ): RequestWrapper {
        return RequestWrapper(
            gson = gson,
            context = context
        )
    }
    @Provides
    @Singleton
    @Named("Authorization")
    fun provideAuthorizationInterceptor(): Interceptor {
        return Interceptor { chain ->
            val request = chain.request()
            val requestBuilder = request.newBuilder()
            requestBuilder.header("Authorization", "Client-ID uxQ_VELbYVLsW95L9HsRfHYvScJ2pS0bjRH4FuW-5yo")
            val newRequest = requestBuilder
                .method(request.method, request.body)
                .build()

            chain.proceed(newRequest)
        }
    }


    @Singleton
    @Provides
    fun provideHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        @Named("Authorization") authorizationInterceptor: Interceptor,
        @ApplicationContext context: Context,
    ): OkHttpClient {
        val okHttpClient = OkHttpClient.Builder()
            .callTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(0, TimeUnit.SECONDS)
            .readTimeout(0, TimeUnit.SECONDS)
            .hostnameVerifier { _, _ -> true }
            .addInterceptor(authorizationInterceptor)
            .addInterceptor(loggingInterceptor)

        return okHttpClient.build()
    }

    @Singleton
    @Provides
    fun provideApiService(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory,
    ): ApiService {
        return provideRetrofit(okHttpClient, gsonConverterFactory, ApiService::class.java)
    }

    private fun <T : WebServiceInterface> provideRetrofit(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory,
        serviceInterface: Class<T>,
    ): T {
        return Retrofit.Builder()
            .baseUrl("https://api.unsplash.com/")
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()
            .create(serviceInterface)
    }
}
