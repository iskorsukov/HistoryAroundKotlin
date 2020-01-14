package com.iskorsukov.historyaround.service.navigation

import androidx.fragment.app.Fragment
import androidx.navigation.NavController

interface NavControllerSource {
    fun navController(fragment: Fragment): NavController
}