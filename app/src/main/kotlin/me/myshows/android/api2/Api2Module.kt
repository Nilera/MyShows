package me.myshows.android.api2

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import dagger.Module
import dagger.Provides
import me.myshows.android.api2.auth.MyShowsAuthClient
import me.myshows.android.api2.auth.impl.MyShowsAuthClientImpl
import me.myshows.android.storage.TokenStorage
import okhttp3.OkHttpClient
import javax.inject.Named
import javax.inject.Singleton

@Module
class Api2Module {

    @Singleton
    @Provides
    fun objectMapper(): ObjectMapper = jacksonObjectMapper()


    @Singleton
    @Provides
    fun authClient(@Named("authHost") host: String, okHttpClient: OkHttpClient,
                   mapper: ObjectMapper, tokenStorage: TokenStorage): MyShowsAuthClient =
            MyShowsAuthClientImpl(host, okHttpClient, mapper, tokenStorage)
}
