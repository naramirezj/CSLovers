package hu.ait.cslovers

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import hu.ait.cslovers.ui.navigation.Screen
import hu.ait.cslovers.ui.screen.feed.FeedScreen
import hu.ait.cslovers.ui.screen.jobdetails.JobDetailsScreen
import hu.ait.cslovers.ui.screen.login.LoginScreen
import hu.ait.cslovers.ui.theme.CSLoversTheme
import hu.ait.cslovers.ui.screen.feed.FeedScreenViewModel
import hu.ait.cslovers.ui.screen.preferences.PreferencesScreen
import hu.ait.cslovers.ui.screen.savedjobs.SavedJobsScreen

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CSLoversTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavGraph()
                }
            }
        }
    }
}

@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(Screen.Login.route) {
            LoginScreen(onLoginSucces = {
                navController.navigate(Screen.Preferences.route)
            })
        }
        composable(Screen.Feed.route,
            arguments = listOf(navArgument("type") { type = NavType.StringType},
                navArgument("level") { type = NavType.StringType}),
            ) {navBackStackEntry ->
            val arguments = navBackStackEntry.arguments
            val type = arguments?.getString("type")
            val level = arguments?.getString("level")
            if (type != null && level != null) {
                FeedScreen(
                    type,
                    level,
                    onNavigateToFeed = { type, level ->
                        navController.navigate("feed/$type/$level") },
                    onNavigateToJobDetails = { id ->
                        navController.navigate("jobdetails/$id")
                    },
                    onNavigateToSavedJobs = {type, level ->
                        navController.navigate("savedjobs/$type/$level")})
            }
        }
        composable(
            Screen.JobDetails.route,
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { navBackStackEntry ->
            val arguments = navBackStackEntry.arguments
            val jobId = arguments?.getInt("id")
            if (jobId != null) {
                JobDetailsScreen(id = jobId)
            }
        }

        composable(Screen.Preferences.route){
            PreferencesScreen(onNavigateToFeed = { type, level ->
                navController.navigate("feed/$type/$level")})
        }

        composable(Screen.SavedJobs.route,
            arguments = listOf(navArgument("type") { type = NavType.StringType},
                        navArgument("level") { type = NavType.StringType}))
        { navBackStackEntry ->
            val arguments = navBackStackEntry.arguments
            val type = arguments?.getString("type")
            val level = arguments?.getString("level")
            if (type != null && level != null) {
                SavedJobsScreen(
                    type,
                    level,
               onNavigateToFeed = { type, level ->
                   navController.navigate("feed/$type/$level")},
               onNavigateToJobDetails = { id ->
                   navController.navigate("jobdetails/$id")},
               onNavigateToSavedJobs = { type, level ->
                   navController.navigate("savedjobs/$type/$level")
               })
            }
        }
    }
}