package com.iskorsukov.historyaround.presentation.view.map

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.iskorsukov.historyaround.HistoryAroundApp
import com.iskorsukov.historyaround.R
import com.iskorsukov.historyaround.databinding.ActivityMapBinding
import com.iskorsukov.historyaround.model.article.ArticleItem
import com.iskorsukov.historyaround.presentation.view.common.error.ErrorDialog
import com.iskorsukov.historyaround.presentation.view.common.viewstate.viewaction.ViewAction
import com.iskorsukov.historyaround.presentation.view.detail.DetailActivity
import com.iskorsukov.historyaround.presentation.view.favorites.FavoritesActivity
import com.iskorsukov.historyaround.presentation.view.map.adapter.ArticleListAdapter
import com.iskorsukov.historyaround.presentation.view.map.utils.toGeoPoint
import com.iskorsukov.historyaround.presentation.view.map.viewaction.CenterOnLocationAction
import com.iskorsukov.historyaround.presentation.view.map.viewaction.NavigateToDetailsAction
import com.iskorsukov.historyaround.presentation.view.map.viewaction.ShowArticleSelectorAction
import com.iskorsukov.historyaround.presentation.view.map.viewstate.MapErrorItem
import com.iskorsukov.historyaround.presentation.view.map.viewstate.viewdata.ArticleItemViewData
import com.iskorsukov.historyaround.presentation.view.preferences.PreferencesActivity
import com.iskorsukov.historyaround.presentation.viewmodel.map.MapViewModel
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.views.CustomZoomButtonsController

class MapActivity: AppCompatActivity() {

    private lateinit var viewModel: MapViewModel

    private lateinit var contentBinding: ActivityMapBinding

    private lateinit var permissionResultLauncher: ActivityResultLauncher<Array<String>>

    private var lastUserLocation: Pair<Double, Double>? = null
    private var lastZoomValue: Double? = null

    companion object {
        private const val LAT_KEY = "lat"
        private const val LON_KEY = "lon"
        private const val ZOOM_KEY = "zoom"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModelFactory = (application as HistoryAroundApp).appComponent.viewModelFactory()
        viewModel = ViewModelProvider(this, viewModelFactory)[MapViewModel::class.java]

        permissionResultLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions(),
            viewModel::onPermissionsResult
        )

        Configuration.getInstance().load(applicationContext, PreferenceManager.getDefaultSharedPreferences(applicationContext))

        contentBinding = ActivityMapBinding.inflate(layoutInflater)
        contentBinding.lifecycleOwner = this
        contentBinding.viewModel = viewModel
        setContentView(contentBinding.root)

        configureToolbar()
        initSettingsAndPermissions(savedInstanceState)
        initMapView()
        tryRestoreInstanceState(savedInstanceState)
        restoreMapLocation()
        observeViewState()
    }

    private fun configureToolbar() {
        setSupportActionBar(contentBinding.toolbar)
        contentBinding.toolbar.title = getString(R.string.app_name)
        val drawerToggle = ActionBarDrawerToggle(
            this,
            contentBinding.drawerLayout,
            contentBinding.toolbar,
            R.string.acc_drawer_open,
            R.string.acc_drawer_close
        )
        contentBinding.drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        contentBinding.navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menuFavorites -> navigateToFavorites()
                R.id.menuPreferences -> navigateToPreferences()
            }
            true
        }
    }

    private fun initSettingsAndPermissions(savedInstanceState: Bundle?) {
        viewModel.checkLocationServicesAvailability().addOnCompleteListener {
            try {
                val locationSettingsResponse = it.result
                requestPermissions(savedInstanceState == null)
            } catch (apiException: ApiException) {
                when (apiException.statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                        val resolvableApiException = apiException as ResolvableApiException
                        val locationServicesResolution = registerForActivityResult(
                            ActivityResultContracts.StartIntentSenderForResult()
                        ) { result ->
                            if (result.resultCode == Activity.RESULT_OK) {
                                requestPermissions(savedInstanceState == null)
                            } else {
                                handleError(MapErrorItem.GOOGLE_SERVICES_ERROR)
                            }
                        }
                        locationServicesResolution.launch(
                            IntentSenderRequest.Builder(resolvableApiException.resolution.intentSender)
                                .build()
                        )
                    }
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                        handleError(MapErrorItem.GOOGLE_SERVICES_ERROR)
                    }
                }
            }
        }
    }

    private fun requestPermissions(isSavedInstanceStateNull: Boolean) {
        val permissions = arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        if (permissions.all { ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED }) {
            if (isSavedInstanceStateNull) viewModel.loadArticles()
        } else {
            permissionResultLauncher.launch(permissions)
        }
    }

    private fun initMapView() {
        contentBinding.mapView.apply {
            setTileSource(TileSourceFactory.MAPNIK)

            setMultiTouchControls(true)
            controller.setZoom(16.0)
            zoomController.setVisibility(CustomZoomButtonsController.Visibility.NEVER)
        }
    }

    private fun observeViewState() {
        viewModel.mapActionLiveEvent.observe(this, this::applyViewAction)
        viewModel.mapErrorLiveEvent.observe(this, this::handleError)
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

    private fun applyViewAction(viewAction: ViewAction<*>) {
        when (viewAction) {
            is ShowArticleSelectorAction -> showArticlesSelector(viewAction.data!!)
            is NavigateToDetailsAction -> navigateToItemDetails(viewAction.data!!)
            is CenterOnLocationAction -> centerOnLocation(viewAction.data!!)
        }
    }

    private fun navigateToItemDetails(articleItem: ArticleItem) {
        val intent = DetailActivity.getIntent(
            this,
            articleItem.pageid,
            articleItem.languageCode
        )
        startActivity(intent)
    }

    private fun navigateToFavorites() {
        startActivity(Intent(this, FavoritesActivity::class.java))
    }

    private fun navigateToPreferences() {
        startActivity(Intent(this, PreferencesActivity::class.java))
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
                requestPermissions(true)
            }

            override fun onCancelClick() {
                dialog.dismiss()
                if (errorItem == MapErrorItem.LOCATION_PERMISSION_ERROR || errorItem == MapErrorItem.GOOGLE_SERVICES_ERROR) {
                    finish()
                }
            }
        }
        dialog.listener = listener
        dialog.show(supportFragmentManager, ErrorDialog.TAG)
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_map, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.menuRefresh) {
            viewModel.onRefresh()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }
}