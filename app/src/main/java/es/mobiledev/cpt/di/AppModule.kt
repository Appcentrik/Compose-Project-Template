package es.mobiledev.cpt.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import es.mobiledev.commonandroid.core.logging.AppLogger
import es.mobiledev.commonandroid.core.logging.LogcatAppLogger
import javax.inject.Singleton

/**
 * Application wide bindings.
 *
 * The logging contract lives in `commonAndroid`, which stays free of any DI framework, and is wired
 * here. A project reporting to a crash platform only has to swap this provider, every ViewModel
 * keeps depending on [AppLogger].
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideAppLogger(): AppLogger = LogcatAppLogger
}
