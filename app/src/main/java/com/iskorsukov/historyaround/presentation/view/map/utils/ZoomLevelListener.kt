package com.iskorsukov.historyaround.presentation.view.map.utils

import org.osmdroid.events.MapListener
import org.osmdroid.events.ScrollEvent
import org.osmdroid.events.ZoomEvent

class ZoomLevelListener(
    private val zoomStep: Double,
    private val onZoomLevelChangedListener: OnZoomLevelChangedListener?
): MapListener {

    private var lastZoomLevel: Double = .0

    override fun onScroll(event: ScrollEvent?): Boolean {
        return false
    }

    override fun onZoom(event: ZoomEvent?): Boolean {
        event?.apply {
            val currentZoomLevel: Double = event.zoomLevel
            if (Math.abs(currentZoomLevel - lastZoomLevel) >= zoomStep) {
                lastZoomLevel = currentZoomLevel
                onZoomLevelChangedListener?.onZoomLevelChanged(lastZoomLevel)
            }
        }
        return true
    }

    interface OnZoomLevelChangedListener {
        fun onZoomLevelChanged(zoomLevel: Double)
    }
}