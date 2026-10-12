package es.mobiledev.data.remote.di

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import es.mobiledev.data.remote.BuildConfig
import es.mobiledev.data.remote.article.ArticleRemoteDataSourceImpl
import es.mobiledev.common.util.AppDispatchers
import es.mobiledev.data.remote.article.ArticleWs
import es.mobiledev.data.remote.util.BASE_URL
import es.mobiledev.data.source.article.ArticleRemoteDataSource
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
object RemoteModule {
    @Provides
    fun interceptorProvider(): Interceptor =
        HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        }

    @Provides
    fun okHttpClientProvider(
        interceptor: Interceptor
    ) = OkHttpClient
        .Builder()
        .connectTimeout(CONNECT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .readTimeout(READ_TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .writeTimeout(WRITE_TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .callTimeout(CALL_TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .addInterceptor(interceptor)
        .build()

    @Provides
    fun moshiProvider(): Moshi =
        Moshi
            .Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

    @Provides
    fun retrofitProvider(
        okHttpClient: OkHttpClient
    ): Retrofit =
        Retrofit
            .Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

    @Provides
    fun provideArticleWs(retrofit: Retrofit): ArticleWs = retrofit.create(ArticleWs::class.java)

    @Provides
    fun articleDataSourceProvider(
        articleWs: ArticleWs,
        appDispatchers: AppDispatchers,
    ) = ArticleRemoteDataSourceImpl(articleWs, appDispatchers) as ArticleRemoteDataSource
}

private const val CONNECT_TIMEOUT_SECONDS = 15L
private const val READ_TIMEOUT_SECONDS = 15L
private const val WRITE_TIMEOUT_SECONDS = 15L
private const val CALL_TIMEOUT_SECONDS = 30L
