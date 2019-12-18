package my.projects.historyaroundkotlin.presentation.view.map.viewstate.viewdata

import my.projects.historyaroundkotlin.presentation.view.common.viewstate.viewdata.ViewData
import java.io.Serializable

class MapViewData(val location: Pair<Double, Double>, val articlesOverlayData: List<ArticlesClusterItem>): ViewData