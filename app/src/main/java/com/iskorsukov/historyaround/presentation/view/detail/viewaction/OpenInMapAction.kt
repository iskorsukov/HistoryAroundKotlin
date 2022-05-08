package com.iskorsukov.historyaround.presentation.view.detail.viewaction

import com.iskorsukov.historyaround.presentation.view.common.viewstate.viewaction.ViewAction

data class OpenInMapAction(private val coords: Pair<Double, Double>):
    ViewAction<Pair<Double, Double>>(coords)