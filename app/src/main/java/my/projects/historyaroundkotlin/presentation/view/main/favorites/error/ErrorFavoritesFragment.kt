package my.projects.historyaroundkotlin.presentation.view.main.favorites.error

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.fragment_error_favorites.*
import my.projects.historyaroundkotlin.R
import my.projects.historyaroundkotlin.presentation.view.common.BaseFragment

class ErrorFavoritesFragment: BaseFragment() {

    override fun fragmentLayout(): Int {
        return R.layout.fragment_error_favorites
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        retry.setOnClickListener { navController().navigate(ErrorFavoritesFragmentDirections.actionErrorFavoritesFragmentToLoadingFavoritesFragment()) }
    }

}