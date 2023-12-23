package hu.ait.cslovers.ui.screen.feed

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.ait.cslovers.data.JobResult
import hu.ait.cslovers.network.JobsAPI
import kotlinx.coroutines.launch
import javax.inject.Inject
import hu.ait.cslovers.data.Result

import hu.ait.cslovers.ui.screen.repository.JobsRepository
import hu.ait.cslovers.ui.screen.repository.SavedJobsRepository
import javax.inject.Singleton

@HiltViewModel
class FeedScreenViewModel @Inject constructor(
    private val jobsAPI: JobsAPI,
    private val savedJobsRepository: SavedJobsRepository,
    private val jobsRepository: JobsRepository
) : ViewModel(){

    var jobsUIState: JobsUiState by mutableStateOf(JobsUiState.Init)
    var jobResultsList: List<Result?> by (mutableStateOf(emptyList()))

    fun getJobs(category: String, level: String, location: String){
        //network call and change UI states properly
        jobsUIState = JobsUiState.Loading
        viewModelScope.launch {
            try {
                val jobsResult = jobsAPI.getJobs(category, level, location, "1")
                jobsUIState = JobsUiState.Success(jobsResult)

            }catch (e:Exception){
                if (e.message?.contains("404") == true) {
                    jobsUIState = JobsUiState.Error("Jobs not found")
                } else {
                    jobsUIState = JobsUiState.Error(e.message ?: "Unknown error occurred")
                }
            }
        }
    }


    fun createJobsList(jobResult: List<Result?>){
       jobsRepository.createJobList(jobResult)
        jobResultsList = jobsRepository.getJobs()
    }


    fun saveJob(savedJob: Result?){
        if (savedJob != null) {
            savedJobsRepository.addSavedJob(savedJob)
        }
    }

    fun removeItem(item: Result?) {
        if (item != null) {
            viewModelScope.launch {
               jobsRepository.removeJob(item)
                jobResultsList = jobsRepository.getJobs()
            }
        }
    }

}

sealed interface JobsUiState {
    object Init : JobsUiState
    object Loading : JobsUiState
    data class Success(val jobsResult: JobResult) : JobsUiState
    data class Error(val errorMessage: String) : JobsUiState
}