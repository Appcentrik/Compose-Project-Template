package es.mobiledev.data.local.di

import android.app.Application
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import es.mobiledev.common.util.AppDispatchers
import es.mobiledev.data.local.AppRoomDatabase
import es.mobiledev.data.local.article.datasource.ArticleLocalDataSourceImpl
import es.mobiledev.data.source.article.ArticleLocalDataSource
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {
    @Provides
    @Singleton
    fun appRoomDatabaseProvider(context: Application) = AppRoomDatabase.buildDatabase(context)

    @Provides
    @Singleton
    fun articleLocalDataSourceProvider(
        roomDatabase: AppRoomDatabase,
        appDispatchers: AppDispatchers,
    ) = ArticleLocalDataSourceImpl(roomDatabase, appDispatchers) as ArticleLocalDataSource
}
