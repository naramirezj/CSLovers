package hu.ait.cslovers.ui.screen.repository

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SavedJobsModule {

    @Provides
    @Singleton
    fun provideSavedJobsRepository(): SavedJobsRepository {
        return SavedJobsRepository()
    }
}