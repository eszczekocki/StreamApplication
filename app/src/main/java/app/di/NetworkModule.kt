package app.di

import app.utils.LocalDateTimeDeserializer
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import data.sources.remote.ApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDateTime
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient():OkHttpClient {
        return OkHttpClient().newBuilder()
            .addInterceptor(
                HttpLoggingInterceptor()
                    .setLevel(HttpLoggingInterceptor.Level.BODY)
            )
            .build()
    }

    @Provides
    @Singleton

    fun provideRetrofit(okHttpClient: OkHttpClient) :Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("https://us-central1-dazn-sandbox.cloudfunctions.net/")
            .addConverterFactory(GsonConverterFactory.create(
                GsonBuilder().registerTypeAdapter(
                    LocalDateTime::class.java,
                    LocalDateTimeDeserializer()
                ).create()
            ))
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }
}