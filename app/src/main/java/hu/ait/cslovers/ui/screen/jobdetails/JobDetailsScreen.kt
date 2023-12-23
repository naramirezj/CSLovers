package hu.ait.cslovers.ui.screen.jobdetails

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import hu.ait.cslovers.R
import hu.ait.cslovers.data.JobResult
import hu.ait.cslovers.data.Result
import hu.ait.cslovers.ui.screen.feed.FeedScreenViewModel
import hu.ait.cslovers.ui.screen.feed.ResultView
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobDetailsScreen(
    id: Int?,
    jobDetailsModel: JobDetailsModel = hiltViewModel()) {
    LaunchedEffect(Unit) {
    jobDetailsModel.getSingleJob(id)
    }
Column {
    when (jobDetailsModel.jobsUIState) {
        is JobUiState.Init -> Text(text = stringResource(R.string.init_message))
        is JobUiState.Error -> Text(text = stringResource(R.string.error_message) + "${(jobDetailsModel.jobsUIState as JobUiState.Error).errorMessage}")
        is JobUiState.Loading -> Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {CircularProgressIndicator()}
        is JobUiState.Success -> DetailsView(
            jobResult = (jobDetailsModel.jobsUIState as JobUiState.Success).jobResult)
    }
}
}

fun intentOpenApplication(context: Context, url: String) {
    val intent = Intent(Intent.ACTION_VIEW)
    intent.data = Uri.parse(url)
    context.startActivity(intent)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsView(
    jobResult: Result,
){
    val context = LocalContext.current
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
        }) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            jobResult?.let { job ->
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = job.name ?: "", style = MaterialTheme.typography.headlineLarge.copy(
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                ), textAlign = TextAlign.Center)
                Text(text = job.type?.uppercase(Locale.ROOT) ?: "", style = MaterialTheme.typography.labelMedium)
                Text(
                    text = stringResource(R.string.publication_date) + "${job.publicationDate ?: ""}",
                    style = MaterialTheme.typography.bodyMedium
                )
                job.locations?.firstOrNull()?.let { location ->
                    Text(
                        text = stringResource(R.string.location) + "${location.name ?: ""}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Image(
                    painter = painterResource(id = R.drawable.applyicon),
                    contentDescription = stringResource(R.string.apply_button),
                    modifier = Modifier
                        .clickable {
                            job.refs?.landingPage?.let { intentOpenApplication(context, it) }
                        }
                        .padding(start = 5.dp)
                        .size(100.dp),
                )
                HTMLContent(job.contents ?: "")
                job.refs?.landingPage?.let { landingPage ->
                }
            }
        }
    }
}
@Composable
fun HTMLContent(htmlContent: String) {
    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = true
                webViewClient = WebViewClient()
                loadDataWithBaseURL(null, htmlContent, "text/html", "utf-8", null)
            }
        }
    )


}