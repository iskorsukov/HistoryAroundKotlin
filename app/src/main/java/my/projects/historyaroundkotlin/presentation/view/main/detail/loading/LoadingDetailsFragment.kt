package my.projects.historyaroundkotlin.presentation.view.main.detail.loading

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import my.projects.historyaroundkotlin.R
import my.projects.historyaroundkotlin.presentation.argument.ArticleDetailsArgument
import my.projects.historyaroundkotlin.presentation.view.common.BaseLoadingFragment
import my.projects.historyaroundkotlin.presentation.viewmodel.main.detail.DetailFlowViewModel
import my.projects.historyaroundkotlin.presentation.view.util.viewModelFactory
import my.projects.historyaroundkotlin.presentation.viewstate.main.detail.DetailErrorStatus
import my.projects.historyaroundkotlin.presentation.viewstate.main.detail.DetailViewState

class LoadingDetailsFragment: BaseLoadingFragment<DetailErrorStatus, DetailViewState>() {

    private lateinit var flowViewModel: DetailFlowViewModel
    private val args: LoadingDetailsFragmentArgs by navArgs()

    override fun fragmentLayout(): Int {
        return R.layout.fragment_loading_details
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        loadArticleDetails()
    }

    private fun initViewModel() {
        flowViewModel = ViewModelProviders.of(this, viewModelFactory())[DetailFlowViewModel::class.java]
    }

    private fun loadArticleDetails() {
        flowViewModel.loadArticleDetails(args.pageid)
        flowViewModel.detailsLiveData.observe(this, Observer {
            applyViewState(it)
        })
    }

    override fun applyErrorState(errors: List<DetailErrorStatus>) {
        navController().navigate(LoadingDetailsFragmentDirections.actionLoadingDetailsFragmentToErrorDetailsFragment())
    }

    override fun applyContentState(viewState: DetailViewState) {
        navController().navigate(LoadingDetailsFragmentDirections.actionLoadingDetailsFragmentToDetailFragment(
            ArticleDetailsArgument(viewState.viewData!!)
        ))
    }
}