package com.febri.core_data.di

import android.content.Context
import androidx.room.Room
import com.febri.core_data.DefaultPokedexRepositoryImpl
import com.febri.core_data.PokedexDataSource
import com.febri.core_data.PokedexRepository
import com.febri.core_data.local.LocalPokedexDataSource
import com.febri.core_data.local.PokemonDao
import com.febri.core_data.local.PokemonDatabase
import com.febri.core_data.remote.ApiServices
import com.febri.core_data.remote.RemotePokedexDataSource
import com.febri.core_data.util.Constants
import com.febri.core_data.util.okHttpClientBuilder
import com.febri.core_data.util.retrofitBuilder
import com.febri.core_data.util.setDefaultTimeout
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class RemoteDataSource

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class LocalDataSource

    @Singleton
    @RemoteDataSource
    @Provides
    fun provideTasksRemoteDataSource(
        apiService: ApiServices
    ): PokedexDataSource {
        return RemotePokedexDataSource(apiService)
    }

    @Singleton
    @LocalDataSource
    @Provides
    fun provideTasksLocalDataSource(
        database: PokemonDatabase
    ): PokedexDataSource {
        return LocalPokedexDataSource(database.pokemonDao())
    }
    @Singleton
    @Provides
    fun bindsPokemonRepository(
        @RemoteDataSource remoteDataSource: PokedexDataSource,
        @LocalDataSource localDataSource: PokedexDataSource
    ): PokedexRepository {
        return DefaultPokedexRepositoryImpl(
            remoteDataSource = remoteDataSource,
            localDataSource = localDataSource)
    }

    @Provides
    fun provideGenreDao(database: PokemonDatabase): PokemonDao {
        return database.pokemonDao()
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): PokemonDatabase {
        return Room.databaseBuilder(
            appContext,
            PokemonDatabase::class.java,
            "pokephinc_database"
        ).allowMainThreadQueries().build()
    }

    @Singleton
    @Provides
    fun provideIoDispatcher() = Dispatchers.IO


    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit = retrofitBuilder {
        baseUrl(Constants.BASE_URL)
        client(okHttpClientBuilder {
            addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            setDefaultTimeout()
        })
        addConverterFactory(GsonConverterFactory.create(Gson()))
    }

    @Provides
    fun provideMovieDBService(retrofit: Retrofit): ApiServices = retrofit.create(ApiServices::class.java)
}