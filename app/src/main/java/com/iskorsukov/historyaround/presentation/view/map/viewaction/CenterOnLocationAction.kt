package com.iskorsukov.historyaround.presentation.view.map.viewaction

import com.iskorsukov.historyaround.presentation.view.common.viewstate.viewaction.ViewAction

data class CenterOnLocationAction(private val location: Pair<Double, Double>):
    ViewAction<Pair<Double, Double>>(location)