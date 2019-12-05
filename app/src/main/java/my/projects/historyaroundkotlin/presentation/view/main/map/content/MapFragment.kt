package my.projects.historyaroundkotlin.presentation.view.main.map.content

import android.os.Bundle
import android.preference.PreferenceManager
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_map.*
import my.projects.historyaroundkotlin.R
import my.projects.historyaroundkotlin.model.article.ArticleItem
import my.projects.historyaroundkotlin.presentation.view.common.BaseFragment
import my.projects.historyaroundkotlin.presentation.view.main.map.content.adapter.ArticleListAdapter
import my.projects.historyaroundkotlin.presentation.view.main.map.content.adapter.ArticleListItemListener
import my.projects.historyaroundkotlin.presentation.view.main.map.content.utils.groupItemsIntoMarkers
import my.projects.historyaroundkotlin.presentation.view.main.map.content.utils.toGeoPoint
import my.projects.historyaroundkotlin.presentation.viewstate.main.map.articles.ArticleItemViewData
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

class MapFragment : BaseFragment(), ArticleListItemListener {

    private val args: MapFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Configuration.getInstance().load(context!!.applicationContext, PreferenceManager.getDefaultSharedPreferences(context!!.applicationContext))
        setHasOptionsMenu(true)
    }

    override fun fragmentLayout(): Int {
        return R.layout.fragment_map
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initMapView()
        configureRecyclerView()
        configureMyLocationFAB()

        showMapMarkers()
    }

    private fun initMapView() {
        mapView.setTileSource(TileSourceFactory.MAPNIK)
        mapView.setMultiTouchControls(true)
        mapView.zoomController.setVisibility(CustomZoomButtonsController.Visibility.NEVER)
        mapView.controller.setCenter(args.mapDataArgument.location.toGeoPoint())
        mapView.controller.setZoom(16.0)
    }

    private fun configureRecyclerView() {
        articlesRecycler.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
    }

    private fun configureMyLocationFAB() {
        val userLocation = args.mapDataArgument.location
        myLocationFAB.setOnClickListener {
            mapView.controller.animateTo(userLocation.toGeoPoint(), 16.0, 1000L)
        }
    }

    private fun showMapMarkers() {
        mapView.overlays.clear()

        val markers = groupItemsIntoMarkers(mapView.zoomLevelDouble, args.mapDataArgument.articles)
        val context = requireContext()
        val markersOverlay: ItemizedOverlayWithFocus<ArticlesOverlayItem> = ItemizedOverlayWithFocus(
            markers,
            context.resources.getDrawable(R.drawable.ic_location_marker),
            context.resources.getDrawable(R.drawable.ic_location_marker_focused),
            context.resources.getColor(R.color.colorPrimary),
            ArticlesOverlayListener(),
            context
        )
        mapView.overlays.add(markersOverlay)
        mapView.addMapListener(ZoomMarkerGroupListener())

        val userLocation = args.mapDataArgument.location
        val userLocationOverlay = IconOverlay(userLocation.toGeoPoint(), resources.getDrawable(R.drawable.ic_my_location))
        mapView.overlays.add(userLocationOverlay)

        val copyrightOverlay = CopyrightOverlay(context)
        mapView.overlays.add(copyrightOverlay)
    }

    override fun onItemSelected(articleItem: ArticleItem) {
        navigateToItemDetails(articleItem)
    }

    private fun navigateToItemDetails(articleItem: ArticleItem) {
        navController().navigate(MapFragmentDirections.actionMapFragmentToDetailFlow(articleItem.pageid))
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        mapView.onPause()
        super.onPause()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_map, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuRefresh -> {
                navController().navigate(MapFragmentDirections.actionMapFragmentToLoadingLocationFragment())
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private inner class ZoomMarkerGroupListener: MapListener {

        private var lastZoomLevel: Double = .0
        private val step = .1

        override fun onScroll(event: ScrollEvent?): Boolean {
            return false
        }

        override fun onZoom(event: ZoomEvent?): Boolean {
            event?.apply {
                val currentZoomLevel: Double = event.zoomLevel
                if (Math.abs(currentZoomLevel - lastZoomLevel) >= step) {
                    showMapMarkers()
                    lastZoomLevel = currentZoomLevel
                }
            }
            return true
        }
    }

    private inner class ArticlesOverlayListener: ItemizedIconOverlay.OnItemGestureListener<ArticlesOverlayItem> {

        private var selectedItem: ArticlesOverlayItem? = null

        override fun onItemLongPress(index: Int, item: ArticlesOverlayItem?): Boolean {
            return true
        }

        override fun onItemSingleTapUp(index: Int, item: ArticlesOverlayItem?): Boolean {
            selectedItem?.apply {
                setMarker(requireContext().resources.getDrawable(R.drawable.ic_location_marker))
            }
            item?.apply {
                selectedItem = this
                setMarker(requireContext().resources.getDrawable(R.drawable.ic_location_marker_focused))
                showArticlesSelector(articleItems)
            }
            return true
        }

        private fun showArticlesSelector(articleItems: List<ArticleItemViewData>) {
            articlesRecycler.visibility = View.VISIBLE
            articlesRecycler.adapter = ArticleListAdapter(articleItems, this@MapFragment)
        }
    }
}