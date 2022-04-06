package com.iskorsukov.historyaround.presentation.view.map

import android.content.Context
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.ui.onNavDestinationSelected
import com.iskorsukov.historyaround.R
import com.iskorsukov.historyaround.databinding.FragmentMapBinding
import com.iskorsukov.historyaround.model.article.ArticleItem
import com.iskorsukov.historyaround.presentation.view.common.error.ErrorDialog
import com.iskorsukov.historyaround.presentation.view.common.fragment.BaseNavViewActionFragment
import com.iskorsukov.historyaround.presentation.view.common.viewstate.viewaction.ViewAction
import com.iskorsukov.historyaround.presentation.view.detail.DetailActivity
import com.iskorsukov.historyaround.presentation.view.map.adapter.ArticleListAdapter
import com.iskorsukov.historyaround.presentation.view.map.utils.toGeoPoint
import com.iskorsukov.historyaround.presentation.view.map.viewaction.CenterOnLocationAction
import com.iskorsukov.historyaround.presentation.view.map.viewaction.NavigateToDetailsAction
import com.iskorsukov.historyaround.presentation.view.map.viewaction.ShowArticleSelectorAction
import com.iskorsukov.historyaround.presentation.view.map.viewstate.MapErrorItem
import com.iskorsukov.historyaround.presentation.view.map.viewstate.viewdata.ArticleItemViewData
import com.iskorsukov.historyaround.presentation.view.util.viewModelFactory
import com.iskorsukov.historyaround.presentation.viewmodel.map.MapViewModel
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.views.CustomZoomButtonsController

class MapFragment : BaseNavViewActionFragment() {

    private lateinit var viewModel: MapViewModel

    private lateinit var contentBinding: FragmentMapBinding

    private var lastUserLocation: Pair<Double, Double>? = null
    private var lastZoomValue: Double? = null

    companion object {
        private const val LAT_KEY = "lat"
        private const val LON_KEY = "lon"
        private const val ZOOM_KEY = "zoom"
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProvider(this, viewModelFactory())[MapViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Configuration.getInstance().load(requireContext().applicationContext, PreferenceManager.getDefaultSharedPreferences(requireContext().applicationContext))
        setHasOptionsMenu(true)
        if (savedInstanceState == null) viewModel.loadArticles()
    }

    override fun inflateContent(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        contentBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_map, container, false)
        contentBinding.lifecycleOwner = this
        contentBinding.viewModel = viewModel
        return contentBinding.root
    }

    override fun titleRes(): Int {
        return R.string.map_fragment_title
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initMapView()
        tryRestoreInstanceState(savedInstanceState)
        restoreMapLocation()
        observeViewState()
    }

    private fun initMapView() {
        contentBinding.mapView.setTileSource(TileSourceFactory.MAPNIK)

        contentBinding.mapView.setMultiTouchControls(true)
        contentBinding.mapView.controller.setZoom(16.0)
        contentBinding.mapView.zoomController.setVisibility(CustomZoomButtonsController.Visibility.NEVER)
    }

    private fun observeViewState() {
        viewModel.mapActionLiveEvent.observe(viewLifecycleOwner, this::applyViewAction)
        viewModel.mapErrorLiveEvent.observe(viewLifecycleOwner, this::handleError)
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
            contentBinding.mapView.controller.setZoom(lastZoomValue!!)
            contentBinding.mapView.setExpectedCenter(lastUserLocation!!.toGeoPoint())
        }
    }

    override fun applyViewAction(viewAction: ViewAction<*>) {
        when (viewAction) {
            is ShowArticleSelectorAction -> showArticlesSelector(viewAction.data!!)
            is NavigateToDetailsAction -> navigateToItemDetails(viewAction.data!!)
            is CenterOnLocationAction -> centerOnLocation(viewAction.data!!)
        }
    }

    private fun navigateToItemDetails(articleItem: ArticleItem) {
        val intent = DetailActivity.getIntent(
            requireContext(),
            articleItem.pageid,
            articleItem.languageCode
        )
        startActivity(intent)
        /*
        navController().navigate(
            MapFragmentDirections.actionMapFragmentToDetailFragment(
                articleItem.pageid,
                articleItem.languageCode
            )
        )*/
    }

    private fun showArticlesSelector(articleItems: List<ArticleItemViewData>) {
        contentBinding.articlesRecycler.visibility = View.VISIBLE
        contentBinding.articlesRecycler.adapter =
            ArticleListAdapter(
                articleItems,
                viewModel
            )
    }

    private fun centerOnLocation(location: Pair<Double, Double>) {
        contentBinding.mapView.controller.setCenter(location.toGeoPoint())
    }

    private fun handleError(errorItem: MapErrorItem) {
        val dialog = ErrorDialog.newInstance(errorItem)
        val listener = object : ErrorDialog.ErrorDialogListener {
            override fun onActionClick() {
                dialog.dismiss()
                viewModel.onRefresh()
            }

            override fun onCancelClick() {
                dialog.dismiss()
            }
        }
        dialog.listener = listener
        dialog.show(childFragmentManager, ErrorDialog.TAG)
    }

    override fun onResume() {
        super.onResume()
        contentBinding.mapView.onResume()
    }

    override fun onPause() {
        contentBinding.mapView.onPause()
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
        lastUserLocation = contentBinding.mapView.mapCenter.latitude to contentBinding.mapView.mapCenter.longitude
        lastZoomValue = contentBinding.mapView.zoomLevelDouble
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
}