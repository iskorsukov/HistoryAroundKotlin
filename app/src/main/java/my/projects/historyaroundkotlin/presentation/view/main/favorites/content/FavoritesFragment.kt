package my.projects.historyaroundkotlin.presentation.view.main.favorites.content

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_favorites.*
import my.projects.historyaroundkotlin.R
import my.projects.historyaroundkotlin.model.article.ArticleItem
import my.projects.historyaroundkotlin.presentation.view.common.BaseFragment
import my.projects.historyaroundkotlin.presentation.view.main.favorites.content.adapter.FavoritesAdapter
import my.projects.historyaroundkotlin.presentation.view.main.favorites.content.adapter.FavoritesListener

class FavoritesFragment : BaseFragment(), FavoritesListener {

    private val args: FavoritesFragmentArgs by navArgs()

    override fun fragmentLayout(): Int {
        return R.layout.fragment_favorites
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureRecyclerView()
        showFavoriteItems()
    }

    private fun configureRecyclerView() {
        favoritesRecycler.layoutManager = LinearLayoutManager(context!!)
    }

    private fun showFavoriteItems() {
        favoritesRecycler.adapter = FavoritesAdapter(args.favoritesArgument.favoriteItems, this)
    }

    override fun onItemSelected(item: ArticleItem) {
        navController().navigate(FavoritesFragmentDirections.actionFavouritesFragmentToDetailFlow(item.pageid))
    }

}