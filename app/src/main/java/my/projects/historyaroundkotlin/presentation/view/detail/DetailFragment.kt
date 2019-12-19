package my.projects.historyaroundkotlin.presentation.view.detail

import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_detail.*
import my.projects.historyaroundkotlin.R
import my.projects.historyaroundkotlin.presentation.view.common.fragment.BaseLCEViewStateActionFragment
import my.projects.historyaroundkotlin.presentation.view.common.viewstate.viewaction.ViewAction
import my.projects.historyaroundkotlin.presentation.view.detail.viewaction.OpenInMapAction
import my.projects.historyaroundkotlin.presentation.view.detail.viewaction.ViewInBrowserAction
import my.projects.historyaroundkotlin.presentation.view.detail.viewstate.DetailErrorItem
import my.projects.historyaroundkotlin.presentation.view.detail.viewstate.viewdata.DetailViewData
import my.projects.historyaroundkotlin.presentation.viewmodel.detail.DetailFlowViewModel

class DetailFragment : BaseLCEViewStateActionFragment<DetailViewData, DetailErrorItem, DetailFlowViewModel>() {

    private val args: DetailFragmentArgs by navArgs()

    override fun viewModelClass(): Class<DetailFlowViewModel> {
        return DetailFlowViewModel::class.java
    }

    override fun contentLayout(): Int {
        return R.layout.fragment_detail
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
    }

    private fun loadDetails() {
        viewModel.loadArticleDetails(args.pageid)
    }

    override fun applyViewAction(viewAction: ViewAction<*>) {
        when (viewAction) {
            is OpenInMapAction -> openInMap(viewAction.data!!)
            is ViewInBrowserAction -> openInBrowser(viewAction.data!!)
        }
    }

    private fun openInMap(latlon: Pair<Double, Double>) {
        val geoIntentString = "geo:${latlon.first},${latlon.second}"
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
        content.apply {
            articleDetailsTitle.text = item.title
            articleDetailsExtract.text = item.extract
            Glide.with(this@DetailFragment).load(item.thumbnail?.url).into(articleDetailsImage)
            openInWikiButton.setOnClickListener {
                viewModel.onViewInBrowserButtonClicked(item.url)
            }
            openInMapButton.setOnClickListener {
                viewModel.onOpenInMapButtonClicked(item.coordinates)
            }
            favoriteButton.setImageDrawable(if (isFavorite) getDrawable(R.drawable.ic_star_black) else getDrawable(R.drawable.ic_star_border_black))
            favoriteButton.setOnClickListener {
                if (isFavorite) {
                    viewModel.removeFromFavorites(item)
                } else {
                    viewModel.addToFavorites(item)
                }
            }
        }
    }

    private fun getDrawable(id: Int): Drawable {
        return context!!.resources.getDrawable(id)
    }
}