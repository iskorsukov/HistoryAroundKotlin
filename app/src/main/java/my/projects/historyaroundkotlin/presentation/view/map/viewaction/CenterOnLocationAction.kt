package my.projects.historyaroundkotlin.presentation.view.map.viewaction

import my.projects.historyaroundkotlin.presentation.view.common.viewstate.viewaction.ViewAction

class CenterOnLocationAction(location: Pair<Double, Double>): ViewAction<Pair<Double, Double>>(location)