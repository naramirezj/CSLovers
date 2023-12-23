package hu.ait.cslovers.ui.screen.repository

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object JobsModule {
    @Provides
    @Singleton
    fun provideJobsRepository(): JobsRepository {
        return JobsRepository()
    }
}