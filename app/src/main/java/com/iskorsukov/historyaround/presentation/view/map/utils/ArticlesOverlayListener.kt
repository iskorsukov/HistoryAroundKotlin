package com.iskorsukov.historyaround.presentation.view.map.utils

import com.iskorsukov.historyaround.R
import com.iskorsukov.historyaround.presentation.view.map.viewstate.viewdata.ArticlesOverlayItem
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.ItemizedIconOverlay

class ArticlesOverlayListener(
    private val mapView: MapView,
    private val articleMarkerListener: ArticleMarkerListener?
): ItemizedIconOverlay.OnItemGestureListener<ArticlesOverlayItem> {

    private var selectedItem: ArticlesOverlayItem? = null

    override fun onItemLongPress(index: Int, item: ArticlesOverlayItem?): Boolean {
        return false
    }

    override fun onItemSingleTapUp(index: Int, item: ArticlesOverlayItem?): Boolean {
        selectedItem?.apply {
            setMarker(mapView.context.getDrawable(R.drawable.ic_location_marker))
        }
        item?.apply {
            selectedItem = this
            setMarker(mapView.context.getDrawable(R.drawable.ic_location_marker_focused))
            articleMarkerListener?.onMarkerSelected(this)
        }
        mapView.invalidate()
        return true
    }

    interface ArticleMarkerListener {
        fun onMarkerSelected(item: ArticlesOverlayItem)
    }
}