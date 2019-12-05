package my.projects.historyaroundkotlin.service.navigation

import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import javax.inject.Inject

class NavControllerSourceImpl @Inject constructor(): NavControllerSource {

    override fun navController(fragment: Fragment): NavController {
        return fragment.requireView().findNavController()
    }
}