package com.iskorsukov.historyaround.presentation.view.map.viewstate.viewdata

import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.OverlayItem
import java.io.Serializable

class ArticlesOverlayItem(aTitle: String?, aSnippet: String?, aGeoPoint: GeoPoint?, val articleItems: List<ArticleItemViewData>) :
    OverlayItem(aTitle, aSnippet, aGeoPoint), Serializable