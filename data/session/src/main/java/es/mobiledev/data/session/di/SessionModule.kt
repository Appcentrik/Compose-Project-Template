package es.mobiledev.data.session.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import es.mobiledev.data.session.PreferencesDataSourceImpl
import es.mobiledev.data.session.util.DATASTORE_FILE_NAME
import es.mobiledev.data.source.preferences.PreferencesDataSource
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SessionModule {
    @Provides
    @Singleton
    fun provideDataStore(
        @ApplicationContext context: Context
    ): DataStore<Preferences> =
        PreferenceDataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler { emptyPreferences() },
            produceFile = { context.preferencesDataStoreFile(name = DATASTORE_FILE_NAME) }
        )

    @Provides
    @Singleton
    fun preferencesDataSourceProvider(preferences: DataStore<Preferences>) = PreferencesDataSourceImpl(preferences = preferences) as PreferencesDataSource
}
