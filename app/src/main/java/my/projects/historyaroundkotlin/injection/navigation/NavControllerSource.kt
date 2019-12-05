package my.projects.historyaroundkotlin.injection.navigation

import androidx.fragment.app.Fragment
import androidx.navigation.NavController

interface NavControllerSource {
    fun navController(fragment: Fragment): NavController
}