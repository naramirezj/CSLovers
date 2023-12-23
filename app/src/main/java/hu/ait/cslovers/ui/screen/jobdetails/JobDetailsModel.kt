package hu.ait.cslovers.ui.screen.jobdetails

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.ait.cslovers.data.JobResult
import hu.ait.cslovers.data.Result
import hu.ait.cslovers.network.JobsAPI
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JobDetailsModel @Inject constructor(
    private val jobsAPI: JobsAPI
) : ViewModel() {
    var jobsUIState: JobUiState by mutableStateOf(JobUiState.Init)

    fun getSingleJob(id: Int?) {
        //network call and change UI states properly
        jobsUIState = JobUiState.Loading
        viewModelScope.launch {
            try {
                val jobsResult = id?.let { jobsAPI.getSingleJob(it.toString()) }
                jobsUIState = jobsResult?.let { JobUiState.Success(it) }!!

            } catch (e: Exception) {
                if (e.message?.contains("404") == true) {
                    jobsUIState = JobUiState.Error("Job not found")
                } else {
                    jobsUIState = JobUiState.Error(e.message ?: "Unknown error occurred")
                }
            }
        }
    }


}

sealed interface JobUiState {
    object Init : JobUiState
    object Loading : JobUiState
    data class Success(val jobResult: Result) : JobUiState
    data class Error(val errorMessage: String) : JobUiState
}