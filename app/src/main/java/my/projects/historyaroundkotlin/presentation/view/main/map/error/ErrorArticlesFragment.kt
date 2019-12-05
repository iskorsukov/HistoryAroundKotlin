package my.projects.historyaroundkotlin.presentation.view.main.map.error

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.fragment_error_articles.*
import my.projects.historyaroundkotlin.R
import my.projects.historyaroundkotlin.presentation.view.common.BaseFragment

class ErrorArticlesFragment: BaseFragment() {

    override fun fragmentLayout(): Int {
        return R.layout.fragment_error_articles
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        retry.setOnClickListener {
            navController().popBackStack()
        }
    }
}