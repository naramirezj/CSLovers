package hu.ait.cslovers.ui.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Feed : Screen("feed/{type}/{level}")
    object JobDetails : Screen("jobdetails/{id}")
    object SavedJobs : Screen("savedjobs/{type}/{level}")
    object Preferences: Screen("preferences")
}