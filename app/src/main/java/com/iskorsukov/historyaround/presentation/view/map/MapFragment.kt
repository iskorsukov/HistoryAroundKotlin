package com.iskorsukov.historyaround.presentation.view.map

import android.os.Bundle
import android.preference.PreferenceManager
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.ui.onNavDestinationSelected
import kotlinx.android.synthetic.main.fragment_map.*
import com.iskorsukov.historyaround.R
import com.iskorsukov.historyaround.databinding.FragmentMapBinding
import com.iskorsukov.historyaround.model.article.ArticleItem
import com.iskorsukov.historyaround.presentation.view.common.fragment.BaseLCEViewStateActionFragment
import com.iskorsukov.historyaround.presentation.view.common.viewstate.viewaction.ViewAction
import com.iskorsukov.historyaround.presentation.view.map.adapter.ArticleListAdapter
import com.iskorsukov.historyaround.presentation.view.map.utils.toGeoPoint
import com.iskorsukov.historyaround.presentation.view.map.viewaction.CenterOnLocationAction
import com.iskorsukov.historyaround.presentation.view.map.viewaction.NavigateToDetailsAction
import com.iskorsukov.historyaround.presentation.view.map.viewaction.ShowArticleSelectorAction
import com.iskorsukov.historyaround.presentation.view.map.viewstate.MapErrorItem
import com.iskorsukov.historyaround.presentation.view.map.viewstate.MapLoadingItem
import com.iskorsukov.historyaround.presentation.view.map.viewstate.viewdata.ArticleItemViewData
import com.iskorsukov.historyaround.presentation.view.map.viewstate.viewdata.ArticlesOverlayItem
import com.iskorsukov.historyaround.presentation.view.map.viewstate.viewdata.MapViewData
import com.iskorsukov.historyaround.presentation.view.map.viewstate.viewdata.toOverlayItem
import com.iskorsukov.historyaround.presentation.viewmodel.map.MapViewModel
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapListener
import org.osmdroid.events.ScrollEvent
import org.osmdroid.events.ZoomEvent
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.overlay.CopyrightOverlay
import org.osmdroid.views.overlay.IconOverlay
import org.osmdroid.views.overlay.ItemizedIconOverlay
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus

class MapFragment : BaseLCEViewStateActionFragment<MapLoadingItem, MapViewData, MapErrorItem, MapViewModel, FragmentMapBinding>() {

    private val zoomLevelListener = ZoomLevelListener(zoomStep = 0.1)

    private var lastUserLocation: Pair<Double, Double>? = null
    private var lastZoomValue: Double? = null

    companion object {
        private const val LAT_KEY = "lat"
        private const val LON_KEY = "lon"
        private const val ZOOM_KEY = "zoom"
    }

    override fun viewModelClass(): Class<MapViewModel> {
        return MapViewModel::class.java
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Configuration.getInstance().load(context!!.applicationContext, PreferenceManager.getDefaultSharedPreferences(context!!.applicationContext))
        setHasOptionsMenu(true)
    }

    override fun contentLayout(): Int {
        return R.layout.fragment_map
    }

    override fun titleRes(): Int {
        return R.string.map_fragment_title
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        initMapView()
        tryRestoreInstanceState(savedInstanceState)
        restoreMapLocation()
        observeViewState()
    }

    private fun initMapView() {
        mapView.removeMapListener(zoomLevelListener)

        mapView.setTileSource(TileSourceFactory.MAPNIK)

        mapView.setMultiTouchControls(true)
        mapView.controller.setZoom(16.0)
        mapView.zoomController.setVisibility(CustomZoomButtonsController.Visibility.NEVER)

        mapView.addMapListener(zoomLevelListener)
    }

    private fun observeViewState() {
        viewModel.mapDataLiveData.observe(viewLifecycleOwner, Observer {
            applyViewState(it)
        })
        viewModel.mapActionLiveData.observe(viewLifecycleOwner, Observer {
            applyViewAction(it)
        })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (lastUserLocation != null) {
            outState.putDouble(LAT_KEY, lastUserLocation!!.first)
            outState.putDouble(LON_KEY, lastUserLocation!!.second)
        }
        if (lastZoomValue != null) {
            outState.putDouble(ZOOM_KEY, lastZoomValue!!)
        }
    }

    private fun tryRestoreInstanceState(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(LAT_KEY) && savedInstanceState.containsKey(LON_KEY)) {
                lastUserLocation = savedInstanceState.getDouble(LAT_KEY) to savedInstanceState.getDouble(LON_KEY)
            }
            if (savedInstanceState.containsKey(ZOOM_KEY)) {
                lastZoomValue = savedInstanceState.getDouble(ZOOM_KEY)
            }
        }
    }

    private fun restoreMapLocation() {
        if (lastUserLocation != null && lastZoomValue != null) {
            mapView.controller.setZoom(lastZoomValue!!)
            mapView.setExpectedCenter(lastUserLocation!!.toGeoPoint())
        }
    }

    override fun showContent(content: MapViewData) {
        contentBinding.fragment = this

        mapView.overlays.clear()

        val context = requireContext()
        val markersOverlay: ItemizedOverlayWithFocus<ArticlesOverlayItem> = ItemizedOverlayWithFocus(
            content.articlesOverlayData.map { it.toOverlayItem() },
            context.getDrawable(R.drawable.ic_location_marker),
            context.getDrawable(R.drawable.ic_location_marker_focused),
            context.resources.getColor(R.color.colorPrimary),
            ArticlesOverlayListener(),
            context
        )
        mapView.overlays.add(markersOverlay)

        val userLocation = content.location
        val userLocationOverlay = IconOverlay(userLocation.toGeoPoint(), context.getDrawable(R.drawable.ic_my_location))
        mapView.overlays.add(userLocationOverlay)

        val copyrightOverlay = CopyrightOverlay(context)
        mapView.overlays.add(copyrightOverlay)

        mapView.invalidate()
    }

    override fun applyViewAction(viewAction: ViewAction<*>) {
        when (viewAction) {
            is ShowArticleSelectorAction -> showArticlesSelector(viewAction.data!!)
            is NavigateToDetailsAction -> navigateToItemDetails(viewAction.data!!)
            is CenterOnLocationAction -> centerOnLocation(viewAction.data!!)
        }
    }

    private fun navigateToItemDetails(articleItem: ArticleItem) {
        navController().navigate(
            MapFragmentDirections.actionMapFragmentToDetailFragment(
                articleItem.pageid,
                articleItem.languageCode
            )
        )
    }

    private fun showArticlesSelector(articleItems: List<ArticleItemViewData>) {
        articlesRecycler.visibility = View.VISIBLE
        articlesRecycler.adapter =
            ArticleListAdapter(
                articleItems,
                viewModel
            )
    }

    private fun centerOnLocation(location: Pair<Double, Double>) {
        mapView.controller.animateTo(location.toGeoPoint())
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        lastUserLocation = mapView.mapCenter.latitude to mapView.mapCenter.longitude
        lastZoomValue = mapView.zoomLevelDouble
        mapView.onPause()
        super.onPause()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_map, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.menuRefresh) {
            viewModel.onRefresh()
            true
        } else {
            item.onNavDestinationSelected(navController()) || super.onOptionsItemSelected(item)
        }
    }

    fun onUserLocationFABClicked() {
        viewModel.onCenterOnUserLocationClicked()
    }

    override fun onErrorRetry() {
        viewModel.onRetry()
    }

    private inner class ZoomLevelListener(private val zoomStep: Double): MapListener {

        private var lastZoomLevel: Double = .0

        override fun onScroll(event: ScrollEvent?): Boolean {
            return false
        }

        override fun onZoom(event: ZoomEvent?): Boolean {
            event?.apply {
                val currentZoomLevel: Double = event.zoomLevel
                if (Math.abs(currentZoomLevel - lastZoomLevel) >= zoomStep) {
                    lastZoomLevel = currentZoomLevel
                    viewModel.onZoomLevelChanged(lastZoomLevel)
                }
            }
            return true
        }
    }

    private inner class ArticlesOverlayListener: ItemizedIconOverlay.OnItemGestureListener<ArticlesOverlayItem> {

        private var selectedItem: ArticlesOverlayItem? = null

        override fun onItemLongPress(index: Int, item: ArticlesOverlayItem?): Boolean {
            return false
        }

        override fun onItemSingleTapUp(index: Int, item: ArticlesOverlayItem?): Boolean {
            selectedItem?.apply {
                setMarker(context!!.getDrawable(R.drawable.ic_location_marker))
            }
            item?.apply {
                selectedItem = this
                setMarker(context!!.getDrawable(R.drawable.ic_location_marker_focused))
                viewModel.onMarkerSelected(this)
            }
            mapView.invalidate()
            return true
        }
    }
}