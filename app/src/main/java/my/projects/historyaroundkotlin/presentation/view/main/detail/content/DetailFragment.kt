package my.projects.historyaroundkotlin.presentation.view.main.detail.content

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_detail.*
import my.projects.historyaroundkotlin.R
import my.projects.historyaroundkotlin.presentation.view.common.BaseFragment

class DetailFragment : BaseFragment() {

    private val args: DetailFragmentArgs by navArgs()

    override fun fragmentLayout(): Int {
        return R.layout.fragment_detail
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showArticleDetails()
    }

    private fun showArticleDetails() {
        args.detailsArgument.articleDetails.apply {
            articleDetailsTitle.text = title
            articleDetailsExtract.text = extract
            Glide.with(this@DetailFragment).load(thumbnail?.url).into(articleDetailsImage)
            openInWikiButton.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
            }
            openInMapButton.setOnClickListener {
                val geoIntentString = "geo:${coordinates.first},${coordinates.second}"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(geoIntentString))
                if (intent.resolveActivity(context!!.packageManager) != null) {
                    startActivity(intent)
                } else {
                    Toast.makeText(context, R.string.no_map_app_error, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}