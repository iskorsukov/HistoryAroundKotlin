package com.iskorsukov.historyaround.presentation.view.detail

import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import kotlinx.android.synthetic.main.fragment_detail.*
import com.iskorsukov.historyaround.R
import com.iskorsukov.historyaround.databinding.FragmentDetailBinding
import com.iskorsukov.historyaround.presentation.view.common.fragment.BaseLCEViewStateActionFragment
import com.iskorsukov.historyaround.presentation.view.common.viewstate.viewaction.ViewAction
import com.iskorsukov.historyaround.presentation.view.detail.viewaction.OpenInMapAction
import com.iskorsukov.historyaround.presentation.view.detail.viewaction.ViewInBrowserAction
import com.iskorsukov.historyaround.presentation.view.detail.viewstate.DetailErrorItem
import com.iskorsukov.historyaround.presentation.view.detail.viewstate.DetailLoadingItem
import com.iskorsukov.historyaround.presentation.view.detail.viewstate.viewdata.DetailViewData
import com.iskorsukov.historyaround.presentation.viewmodel.detail.DetailViewModel

class DetailFragment : BaseLCEViewStateActionFragment<DetailLoadingItem, DetailViewData, DetailErrorItem, DetailViewModel, FragmentDetailBinding>() {

    private val args: DetailFragmentArgs by navArgs()

    override fun viewModelClass(): Class<DetailViewModel> {
        return DetailViewModel::class.java
    }

    override fun contentLayout(): Int {
        return R.layout.fragment_detail
    }

    override fun titleRes(): Int {
        return R.string.details_fragment_title
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()
        observeViewState()

        loadDetails()
    }

    private fun observeViewState() {
        viewModel.viewStateLiveData.observe(viewLifecycleOwner, Observer {
            applyViewState(it)
        })
        viewModel.viewActionLiveData.observe(viewLifecycleOwner, Observer {
            applyViewAction(it)
        })
    }

    private fun loadDetails() {
        viewModel.loadArticleDetails(args.pageid, args.languageCode)
    }

    override fun applyViewAction(viewAction: ViewAction<*>) {
        when (viewAction) {
            is OpenInMapAction -> openInMap(viewAction.data!!)
            is ViewInBrowserAction -> openInBrowser(viewAction.data!!)
        }
    }

    private fun openInMap(latlon: Pair<Double, Double>) {
        val geoIntentString = "geo:${latlon.first},${latlon.second}?q=${latlon.first},${latlon.second}"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(geoIntentString))
        if (intent.resolveActivity(context!!.packageManager) != null) {
            startActivity(intent)
        } else {
            Toast.makeText(context, R.string.no_map_app_error, Toast.LENGTH_LONG).show()
        }
    }

    private fun openInBrowser(uri: Uri) {
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }

    override fun showContent(content: DetailViewData) {
        contentBinding.viewData = content
        content.apply {
            openInWikiButton.setOnClickListener {
                viewModel.onViewInBrowserButtonClicked(item.url)
            }
            openInMapButton.setOnClickListener {
                viewModel.onOpenInMapButtonClicked(item.coordinates)
            }
            favoriteButton.setImageDrawable(if (isFavorite) getDrawable(R.drawable.ic_star) else getDrawable(R.drawable.ic_star_border))
            favoriteButton.setOnClickListener {
                if (isFavorite) {
                    viewModel.removeFromFavorites(item)
                } else {
                    viewModel.addToFavorites(item)
                }
            }
        }
    }

    override fun onErrorRetry() {
        loadDetails()
    }

    private fun getDrawable(id: Int): Drawable {
        return context!!.resources.getDrawable(id, context!!.theme)
    }
}