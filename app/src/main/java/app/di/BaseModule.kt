package app.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BaseModule {

    @Provides
    @Singleton
    fun provideIoDispatcher(): CoroutineDispatcher {
        return Dispatchers.IO
    }
}