package my.projects.historyaroundkotlin.presentation.view.main.map.loading

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import my.projects.historyaroundkotlin.R
import my.projects.historyaroundkotlin.presentation.argument.LocationArgument
import my.projects.historyaroundkotlin.presentation.view.common.BaseLoadingFragment
import my.projects.historyaroundkotlin.presentation.viewmodel.main.map.MapFlowViewModel
import my.projects.historyaroundkotlin.presentation.view.util.viewModelFactory
import my.projects.historyaroundkotlin.presentation.viewstate.main.map.location.LocationErrorStatus
import my.projects.historyaroundkotlin.presentation.viewstate.main.map.location.LocationViewState

class LoadingLocationFragment: BaseLoadingFragment<LocationErrorStatus, LocationViewState>() {

    private lateinit var flowViewModel: MapFlowViewModel

    override fun fragmentLayout(): Int {
        return R.layout.fragment_loading_location
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        observeLocation()
    }

    private fun initViewModel() {
        flowViewModel = ViewModelProviders.of(this, viewModelFactory())[MapFlowViewModel::class.java]
    }

    private fun observeLocation() {
        flowViewModel.observeLocation().observe(this, Observer {
            applyViewState(it)
        })
    }

    override fun applyErrorState(errors: List<LocationErrorStatus>) {
        navController().navigate(LoadingLocationFragmentDirections.actionLoadingLocationFragmentToErrorLocationFragment())
    }

    override fun applyContentState(viewState: LocationViewState) {
        navController().navigate(LoadingLocationFragmentDirections.actionLoadingLocationFragmentToLoadingArticlesFragment(
            LocationArgument(viewState.location!!)
        ))
    }
}