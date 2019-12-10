package my.projects.historyaroundkotlin.presentation.view.main.detail.content

import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_detail.*
import my.projects.historyaroundkotlin.R
import my.projects.historyaroundkotlin.model.article.ArticleItem
import my.projects.historyaroundkotlin.presentation.view.common.BaseFragment
import my.projects.historyaroundkotlin.presentation.view.util.observeSingle
import my.projects.historyaroundkotlin.presentation.view.util.viewModelFactory
import my.projects.historyaroundkotlin.presentation.viewmodel.main.detail.DetailFlowViewModel
import my.projects.historyaroundkotlin.presentation.viewstate.main.detail.ArticleDetailsViewData

class DetailFragment : BaseFragment() {

    private lateinit var viewModel: DetailFlowViewModel
    private val args: DetailFragmentArgs by navArgs()

    override fun fragmentLayout(): Int {
        return R.layout.fragment_detail
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        viewModel.detailsLiveData.observe(this, Observer {
            showArticleDetails(it.viewData!!)
        })
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this, viewModelFactory())[DetailFlowViewModel::class.java]
    }

    private fun showArticleDetails(viewData: ArticleDetailsViewData) {
        viewData.apply {
            articleDetailsTitle.text = item.title
            articleDetailsExtract.text = item.extract
            Glide.with(this@DetailFragment).load(item.thumbnail?.url).into(articleDetailsImage)
            openInWikiButton.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(item.url))
                startActivity(intent)
            }
            openInMapButton.setOnClickListener {
                val geoIntentString = "geo:${item.coordinates.first},${item.coordinates.second}"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(geoIntentString))
                if (intent.resolveActivity(context!!.packageManager) != null) {
                    startActivity(intent)
                } else {
                    Toast.makeText(context, R.string.no_map_app_error, Toast.LENGTH_LONG).show()
                }
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