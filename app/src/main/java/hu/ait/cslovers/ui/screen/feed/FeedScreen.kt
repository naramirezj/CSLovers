package hu.ait.cslovers.ui.screen.feed

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import hu.ait.cslovers.R
import hu.ait.cslovers.data.Result
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import hu.ait.cslovers.data.JobResult

@Composable
fun FeedScreen(
    jobType: String?,
    jobLevel: String?,
    feedScreenViewModel: FeedScreenViewModel = hiltViewModel(),
    onNavigateToFeed: (String?, String?) -> Unit,
    onNavigateToJobDetails: (Int?) -> Unit = {},
    onNavigateToSavedJobs: (String?, String?) -> Unit) {

    LaunchedEffect(Unit) {
        if (jobType != null) {
            if (jobLevel != null) {
                feedScreenViewModel.getJobs(jobType, jobLevel,"United States")
            }
        }
    }
    when (feedScreenViewModel.jobsUIState) {
        is JobsUiState.Init -> Text(text = stringResource(R.string.init_message))
        is JobsUiState.Error -> Text(text = stringResource(R.string.error_message) + "${(feedScreenViewModel.jobsUIState as JobsUiState.Error).errorMessage}")
        is JobsUiState.Loading -> Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {CircularProgressIndicator()}
        is JobsUiState.Success -> ResultView(
            jobType,
            jobLevel,
            jobResult = (feedScreenViewModel.jobsUIState as JobsUiState.Success).jobsResult,
            onNavigateToJobDetails = onNavigateToJobDetails,
            onNavigateToSavedJobs = onNavigateToSavedJobs,
            onNavigateToFeed = onNavigateToFeed
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultView(
    jobType: String?,
    jobLevel: String?,
    jobResult: JobResult,
    onNavigateToFeed: (String?, String?) -> Unit,
    onNavigateToJobDetails: (Int?) -> Unit = {},
    onNavigateToSavedJobs: (String?, String?) -> Unit,
    feedScreenViewModel: FeedScreenViewModel = hiltViewModel(),
    ){

    jobResult.results?.let { feedScreenViewModel.createJobsList(it) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.csloverslogohorizontal),
                            contentDescription = stringResource(R.string.logo_description)
                        )
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )

            )
        },
        bottomBar = {
            BottomAppBar() {
                Row(modifier = Modifier
                    .fillMaxHeight()
                    .padding(16.dp)) {
                    IconButton(onClick = { onNavigateToFeed(jobType, jobLevel) }) {
                        Icon(Icons.Filled.Menu, contentDescription = stringResource(R.string.feed_logo))
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    IconButton(onClick = { onNavigateToSavedJobs(jobType, jobLevel) }) {
                        Icon(Icons.Filled.Favorite, contentDescription = stringResource(R.string.saved_jobs_logo))
                    }
                }
            }
        }) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = stringResource(R.string.found_message) + jobResult.total + stringResource(R.string.jobs_message))
            if(jobResult.results?.isEmpty()!!){
                Text(text = stringResource(R.string.no_jobs_message))
            }else{
                LazyColumn {
                    items(feedScreenViewModel.jobResultsList) { it ->
                        JobCard(
                            jobResult = it,
                            onClickCard = onNavigateToJobDetails,
                            onRemoveItem = {feedScreenViewModel.removeItem(it)},
                            onSaveItem = { feedScreenViewModel.saveJob(it) },
                        )}
                }
            }
        }
    }
}

@Composable
fun JobCard(
    jobResult: Result?,
    onClickCard: (Int?) -> Unit = {},
    onRemoveItem: () -> Unit = {},
    onSaveItem: () -> Unit = {},
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        modifier = Modifier
            .padding(5.dp)
            .clickable { onClickCard(jobResult?.id) }
    ) {
        Row(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                if (jobResult != null) {
                    jobResult.name?.let { Text(it,
                        modifier = Modifier.fillMaxWidth(0.45f),
                        style = TextStyle(
                            fontSize = 20.sp,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                    ) }
                }
                if (jobResult != null) {
                    jobResult.company?.name?.let { Text(it,
                        modifier = Modifier.fillMaxWidth(0.3f)) }
                }
            }
            Spacer(modifier = Modifier.fillMaxSize(0.35f))
            Image(
                painter = painterResource(id = R.drawable.checkmark),
                contentDescription = stringResource(R.string.checkmark_logo),
                modifier = Modifier
                    .clickable {
                        onSaveItem()
                        onRemoveItem()
                    }
                    .padding(start = 10.dp)
                    .size(34.dp),
            )
            Spacer(modifier = Modifier.fillMaxSize(0.15f))
            Image(
                painter = painterResource(id = R.drawable.cross),
                contentDescription = stringResource(R.string.cross_logo),
                modifier = Modifier
                    .clickable {
                        onRemoveItem()
                    }
                    .padding(start = 10.dp)
                    .size(34.dp),
            )
        }
    }
}