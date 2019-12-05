package my.projects.historyaroundkotlin.presentation.view.main.map.loading

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import my.projects.historyaroundkotlin.R
import my.projects.historyaroundkotlin.presentation.argument.MapDataArgument
import my.projects.historyaroundkotlin.presentation.view.common.BaseLoadingFragment
import my.projects.historyaroundkotlin.presentation.viewmodel.main.map.MapFlowViewModel
import my.projects.historyaroundkotlin.presentation.view.util.viewModelFactory
import my.projects.historyaroundkotlin.presentation.viewstate.main.map.articles.ArticlesErrorStatus
import my.projects.historyaroundkotlin.presentation.viewstate.main.map.articles.ArticlesViewState

class LoadingArticlesFragment: BaseLoadingFragment<ArticlesErrorStatus, ArticlesViewState>() {

    private lateinit var flowViewModel: MapFlowViewModel
    private val args: LoadingArticlesFragmentArgs by navArgs()

    override fun fragmentLayout(): Int {
        return R.layout.fragment_loading_articles
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        loadArticles()
    }

    private fun initViewModel() {
        flowViewModel = ViewModelProviders.of(this, viewModelFactory())[MapFlowViewModel::class.java]
    }

    private fun loadArticles() {
        flowViewModel.loadArticles(args.locationArgument.location).observe(this, Observer {
            applyViewState(it)
        })
    }

    override fun applyErrorState(errors: List<ArticlesErrorStatus>) {
        navController().navigate(LoadingArticlesFragmentDirections.actionLoadingArticlesFragmentToErrorArticlesFragment())
    }

    override fun applyContentState(viewState: ArticlesViewState) {
        navController().navigate(LoadingArticlesFragmentDirections.actionLoadingArticlesFragmentToMapFragment(
            MapDataArgument(args.locationArgument.location, ArrayList(viewState.items!!.toMutableList()))
        ))
    }
}