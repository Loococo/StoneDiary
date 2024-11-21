package app.loococo.stonediary.di.network

import app.loococo.stonediary.BuildConfig
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RetrofitModule {

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder()
        .setPrettyPrinting()
        .setLenient()
        .serializeNulls()
        .create()

    @Provides
    fun provideBaseOkHttpClientBuilder(): OkHttpClient.Builder {
        return OkHttpClient.Builder().apply {
            addInterceptor(HttpLoggingInterceptor().apply {
                level = if (BuildConfig.DEBUG) {
                    HttpLoggingInterceptor.Level.BODY
                } else {
                    HttpLoggingInterceptor.Level.NONE
                }
            })
            writeTimeout(30, TimeUnit.SECONDS)
            readTimeout(30, TimeUnit.SECONDS)
            connectTimeout(30, TimeUnit.SECONDS)
        }
    }

    @AuthNetworkClient
    @Provides
    fun provideAuthNetworkClient(
        clientBuilder: OkHttpClient.Builder,
    ): OkHttpClient = clientBuilder
        .addInterceptor { chain ->
            val originalRequest = chain.request()
            val authenticatedRequest = originalRequest.newBuilder()
                .removeHeader("Authorization")
                .addHeader("Authorization", "Bearer test")
                .build()
            chain.proceed(authenticatedRequest)
        }.build()

    @OtherNetworkClient
    @Provides
    fun provideOtherNetworkClient(
        clientBuilder: OkHttpClient.Builder
    ): OkHttpClient = clientBuilder.build()

    @AuthNetworkClient
    @Provides
    fun provideAuthRetrofit(
        @AuthNetworkClient client: OkHttpClient,
        gson: Gson
    ): Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.API_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(client)
        .build()

    @OtherNetworkClient
    @Provides
    fun provideOtherRetrofit(
        @OtherNetworkClient client: OkHttpClient,
        gson: Gson
    ): Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.API_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(client)
        .build()
}
