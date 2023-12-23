package hu.ait.cslovers.ui.screen.repository

import hu.ait.cslovers.data.Result
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class JobsRepository @Inject constructor(){
    private var jobsList = mutableListOf<Result?>()

    fun createJobList(jobsResult: List<Result?>){
        if(jobsList.isEmpty()) {
            jobsList = jobsResult.toMutableList()
        }
    }
    fun getJobs(): List<Result?> {
        return jobsList.toList()
    }

    suspend fun removeJob(savedJob: Result){
        jobsList.remove(savedJob)
    }
}