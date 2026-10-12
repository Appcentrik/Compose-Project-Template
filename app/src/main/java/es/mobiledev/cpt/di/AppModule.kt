package es.mobiledev.cpt.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import es.mobiledev.common.util.AppDispatchers
import es.mobiledev.commonandroid.core.logging.AppLogger
import es.mobiledev.commonandroid.core.logging.LogcatAppLogger
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideAppDispatchers(): AppDispatchers =
        AppDispatchers(
            main = Dispatchers.Main.immediate,
            default = Dispatchers.Default,
            io = Dispatchers.IO,
        )

    @Provides
    @Singleton
    fun provideAppLogger(): AppLogger = LogcatAppLogger
}
