package hu.ait.cslovers.ui.screen.savedjobs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.ait.cslovers.network.JobsAPI
import hu.ait.cslovers.ui.screen.feed.FeedScreenViewModel
import hu.ait.cslovers.ui.screen.repository.SavedJobsRepository
import javax.inject.Inject
import javax.inject.Singleton
import hu.ait.cslovers.data.Result
import kotlinx.coroutines.launch

@HiltViewModel
class SavedJobsModel @Inject constructor(
private val savedJobsRepository: SavedJobsRepository): ViewModel(){
    var savedJobs: List<Result?> by mutableStateOf(emptyList())

    init{
        savedJobs = savedJobsRepository.getSavedJobs()
    }

    fun getJobs(): List<Result?>{
        return savedJobs
    }

    fun removeSavedJob(item: Result?){
        if (item != null) {
            viewModelScope.launch {
                savedJobsRepository.removeSavedJob(item)
                // Update savedJobs MutableState after removal
                savedJobs = savedJobsRepository.getSavedJobs()
            }
        }
    }

    fun getSizeList(): Int{
        return savedJobsRepository.getSavedJobs().size
    }

}