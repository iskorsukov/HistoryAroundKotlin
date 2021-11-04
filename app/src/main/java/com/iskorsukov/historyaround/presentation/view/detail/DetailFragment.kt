package com.iskorsukov.historyaround.presentation.view.detail

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.iskorsukov.historyaround.R
import com.iskorsukov.historyaround.databinding.FragmentDetailBinding
import com.iskorsukov.historyaround.presentation.view.common.fragment.BaseNavViewActionFragment
import com.iskorsukov.historyaround.presentation.view.common.viewstate.viewaction.ViewAction
import com.iskorsukov.historyaround.presentation.view.detail.viewaction.OpenInMapAction
import com.iskorsukov.historyaround.presentation.view.detail.viewaction.ViewInBrowserAction
import com.iskorsukov.historyaround.presentation.view.detail.viewstate.DetailErrorItem
import com.iskorsukov.historyaround.presentation.view.detail.viewstate.viewdata.DetailViewData
import com.iskorsukov.historyaround.presentation.view.util.viewModelFactory
import com.iskorsukov.historyaround.presentation.viewmodel.detail.DetailViewModel

class DetailFragment : BaseNavViewActionFragment() {

    private lateinit var viewModel: DetailViewModel

    private lateinit var contentBinding: FragmentDetailBinding

    private val args: DetailFragmentArgs by navArgs()

    override fun inflateContent(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        contentBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)
        return contentBinding.root
    }

    override fun titleRes(): Int {
        return R.string.details_fragment_title
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProvider(this, viewModelFactory())[DetailViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewState()
        loadDetails()
    }

    private fun observeViewState() {
        viewModel.detailDataLiveData.observe(viewLifecycleOwner, this::showContent)
        viewModel.detailErrorLiveData.observe(viewLifecycleOwner, this::handleError)
        viewModel.detailActionLiveData.observe(viewLifecycleOwner, this::applyViewAction)
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
        if (intent.resolveActivity(requireContext().packageManager) != null) {
            startActivity(intent)
        } else {
            Toast.makeText(context, R.string.no_map_app_error, Toast.LENGTH_LONG).show()
        }
    }

    private fun openInBrowser(uri: Uri) {
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }

    private fun showContent(content: DetailViewData) {
        contentBinding.viewData = content
        content.apply {
            contentBinding.openInWikiButton.setOnClickListener {
                viewModel.onViewInBrowserButtonClicked(item.url)
            }
            contentBinding.openInMapButton.setOnClickListener {
                viewModel.onOpenInMapButtonClicked(item.coordinates)
            }
            contentBinding.favoriteButton.setImageDrawable(if (isFavorite) getDrawable(R.drawable.ic_star) else getDrawable(R.drawable.ic_star_border))
            contentBinding.favoriteButton.setOnClickListener {
                if (isFavorite) {
                    viewModel.removeFromFavorites(item)
                } else {
                    viewModel.addToFavorites(item)
                }
            }
        }
    }

    private fun handleError(error: DetailErrorItem) {
        TODO("Show error dialog")
    }

    private fun getDrawable(id: Int): Drawable {
        return requireContext().resources.getDrawable(id, requireContext().theme)
    }
}