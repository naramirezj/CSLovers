package hu.ait.cslovers.ui.screen.repository

import hu.ait.cslovers.data.Result

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SavedJobsRepository @Inject constructor(){
    private val savedJobsList = mutableListOf<Result?>()

    fun addSavedJob(savedJob: Result) {
        savedJobsList.add(savedJob)
    }

    fun getSavedJobs(): List<Result?> {
        return savedJobsList.toList()
    }

    suspend fun removeSavedJob(savedJob: Result){
        savedJobsList.remove(savedJob)
    }
}