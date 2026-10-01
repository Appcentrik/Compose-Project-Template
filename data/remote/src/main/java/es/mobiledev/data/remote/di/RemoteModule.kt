package es.mobiledev.data.remote.di

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import es.mobiledev.data.remote.BuildConfig
import es.mobiledev.data.remote.article.ArticleRemoteDataSourceImpl
import es.mobiledev.data.remote.article.ArticleWs
import es.mobiledev.data.remote.util.BASE_URL
import es.mobiledev.data.source.article.ArticleRemoteDataSource
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

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
    fun articleDataSourceProvider(articleWs: ArticleWs) = ArticleRemoteDataSourceImpl(articleWs) as ArticleRemoteDataSource
}
