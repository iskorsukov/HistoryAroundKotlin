package com.iskorsukov.historyaround.presentation.view.detail

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.appbar.AppBarLayout
import com.iskorsukov.historyaround.HistoryAroundApp
import com.iskorsukov.historyaround.R
import com.iskorsukov.historyaround.databinding.ActivityDetailBinding
import com.iskorsukov.historyaround.presentation.view.common.appbar.AppBarStateChangeListener
import com.iskorsukov.historyaround.presentation.view.common.error.ErrorDialog
import com.iskorsukov.historyaround.presentation.view.common.viewstate.viewaction.ViewAction
import com.iskorsukov.historyaround.presentation.view.detail.viewaction.OpenInMapAction
import com.iskorsukov.historyaround.presentation.view.detail.viewaction.ViewInBrowserAction
import com.iskorsukov.historyaround.presentation.view.detail.viewstate.DetailErrorItem
import com.iskorsukov.historyaround.presentation.view.detail.viewstate.viewdata.DetailViewData
import com.iskorsukov.historyaround.presentation.viewmodel.detail.DetailViewModel

class DetailActivity: AppCompatActivity() {

    private lateinit var viewModel: DetailViewModel

    private lateinit var contentBinding: ActivityDetailBinding

    private var isMenuAdd = true
    private var isShowToolbarMenu = false

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (!isShowToolbarMenu) {
            return super.onCreateOptionsMenu(menu)
        }

        if (isMenuAdd) {
            menuInflater.inflate(R.menu.menu_detail_add, menu)
        } else {
            menuInflater.inflate(R.menu.menu_detail_remove, menu)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuFavorites -> {
                viewModel.onFavoriteButtonClicked()
                return true
            }
            R.id.menuMap -> {
                viewModel.onOpenInMapButtonClicked()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(
            this,
            (application as HistoryAroundApp).appComponent.viewModelFactory()
        )[DetailViewModel::class.java]
        contentBinding = ActivityDetailBinding.inflate(layoutInflater)
        contentBinding.lifecycleOwner = this
        contentBinding.viewModel = viewModel
        setContentView(contentBinding.root)
        setSupportActionBar(contentBinding.toolbar)

        if (!intent.hasExtra(PAGE_ID_EXTRA) || !intent.hasExtra(LANGUAGE_CODE_EXTRA))
            finish()

        contentBinding.appbar.addOnOffsetChangedListener(object : AppBarStateChangeListener() {
            override fun onStateChange(layout: AppBarLayout, state: State) {
                if (state == State.COLLAPSED) {
                    contentBinding.toolbarFabContainer.visibility = View.INVISIBLE
                    isShowToolbarMenu = true
                    invalidateOptionsMenu()
                } else {
                    contentBinding.toolbarFabContainer.visibility = View.VISIBLE
                    isShowToolbarMenu = false
                    invalidateOptionsMenu()
                }
            }
        })

        if (savedInstanceState == null) loadDetails()
        observeViewState()
    }

    private fun observeViewState() {
        viewModel.detailErrorLiveEvent.observe(this, this::handleError)
        viewModel.detailActionLiveEvent.observe(this, this::applyViewAction)
        viewModel.detailDataLiveData.observe(this, this::updateUICustom)
    }

    private fun loadDetails() {
        viewModel.loadArticleDetails(
            intent.getStringExtra(PAGE_ID_EXTRA)!!,
            intent.getStringExtra(LANGUAGE_CODE_EXTRA)!!
        )
    }

    private fun applyViewAction(viewAction: ViewAction<*>) {
        when (viewAction) {
            is OpenInMapAction -> openInMap(viewAction.data!!)
            is ViewInBrowserAction -> openInBrowser(viewAction.data!!)
        }
    }

    private fun updateUICustom(viewData: DetailViewData) {
        if (viewData.isFavorite && isMenuAdd) {
            isMenuAdd = false
            invalidateOptionsMenu()
        } else if (!viewData.isFavorite && !isMenuAdd) {
            isMenuAdd = true
            invalidateOptionsMenu()
        }
    }

    private fun openInMap(latlon: Pair<Double, Double>) {
        val geoIntentString = "geo:${latlon.first},${latlon.second}?q=${latlon.first},${latlon.second}"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(geoIntentString))
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        } else {
            Toast.makeText(this, R.string.no_map_app_error, Toast.LENGTH_LONG).show()
        }
    }

    private fun openInBrowser(uri: Uri) {
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }

    private fun handleError(error: DetailErrorItem) {
        val dialog = ErrorDialog.newInstance(error)
        val listener = object : ErrorDialog.ErrorDialogListener {
            override fun onActionClick() {
                dialog.dismiss()
                loadDetails()
            }

            override fun onCancelClick() {
                dialog.dismiss()
                finish()
            }
        }
        dialog.listener = listener
        dialog.show(supportFragmentManager, ErrorDialog.TAG)
    }

    companion object {
        const val PAGE_ID_EXTRA = "pageid"
        const val LANGUAGE_CODE_EXTRA = "languageCode"

        fun getIntent(context: Context, pageid: String, languageCode: String): Intent {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(PAGE_ID_EXTRA, pageid)
            intent.putExtra(LANGUAGE_CODE_EXTRA, languageCode)
            return intent
        }
    }

}