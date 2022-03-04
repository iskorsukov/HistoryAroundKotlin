package com.iskorsukov.historyaround.presentation.view.common.databinding

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.iskorsukov.historyaround.R
import com.iskorsukov.historyaround.presentation.view.common.adapter.ItemListener
import com.iskorsukov.historyaround.presentation.view.favorites.adapter.FavoritesAdapter
import com.iskorsukov.historyaround.presentation.view.favorites.adapter.FavoritesListener
import com.iskorsukov.historyaround.presentation.view.favorites.viewstate.viewdata.FavoritesViewData
import com.iskorsukov.historyaround.presentation.view.map.adapter.ArticleListItemListener
import com.iskorsukov.historyaround.presentation.view.map.utils.ArticlesOverlayListener
import com.iskorsukov.historyaround.presentation.view.map.utils.ZoomLevelListener
import com.iskorsukov.historyaround.presentation.view.map.utils.toGeoPoint
import com.iskorsukov.historyaround.presentation.view.map.viewstate.viewdata.ArticlesOverlayItem
import com.iskorsukov.historyaround.presentation.view.map.viewstate.viewdata.MapViewData
import com.iskorsukov.historyaround.presentation.view.map.viewstate.viewdata.toOverlayItem
import com.iskorsukov.historyaround.presentation.view.permission.adapter.PermissionsAdapter
import com.iskorsukov.historyaround.presentation.view.permission.viewstate.viewdata.PermissionRationale
import com.iskorsukov.historyaround.presentation.view.permission.viewstate.viewdata.PermissionsViewData
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.CopyrightOverlay
import org.osmdroid.views.overlay.IconOverlay
import org.osmdroid.views.overlay.ItemizedIconOverlay
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus

@BindingAdapter("android:text")
fun setText(textView: TextView, @StringRes textRes: Int) {
    if (textRes != 0) {
        textView.setText(textRes)
    }
}

@BindingAdapter("android:src")
fun setImgSrcUri(imageView: ImageView, url: String?) {
    Glide
        .with(imageView.context)
        .load(url)
        .centerInside()
        .placeholder(R.drawable.ic_image_placeholder)
        .error(R.drawable.ic_broken_image_placeholder)
        .listener(object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                imageView.scaleType = ImageView.ScaleType.CENTER_CROP
                return false
            }
        })
        .into(imageView)
}

@BindingAdapter("android:src")
fun setImgSrcId(imageView: ImageView, resid: Int) {
    if (resid != 0) {
        imageView.setImageDrawable(imageView.context.getDrawable(resid))
    } else {
        imageView.setImageDrawable(imageView.context.getDrawable(R.drawable.ic_image_placeholder))
    }
}

@BindingAdapter("app:visibility")
fun setVisibility(view: View, isVisible: Boolean) {
    view.visibility = if (isVisible) View.VISIBLE else View.GONE
}

@BindingAdapter("app:visibility")
fun setVisibility(viewGroup: ViewGroup, isVisible: Boolean) {
    viewGroup.visibility = if (isVisible) View.VISIBLE else View.GONE
}

@BindingAdapter(
    "app:permissionsRecyclerItems", "app:recyclerItemListener",
    requireAll = true
)
fun setPermissionsRecyclerViewItems(
    recyclerView: RecyclerView,
    viewData: PermissionsViewData?,
    itemListener: ItemListener?
) {
    val items = viewData?.rationaleList

    while (recyclerView.itemDecorationCount > 0) {
        recyclerView.removeItemDecorationAt(0)
    }

    if (items.isNullOrEmpty()) {
        recyclerView.adapter = null
        return
    }

    recyclerView.addItemDecoration(
        DividerItemDecoration(recyclerView.context, RecyclerView.VERTICAL)
    )
    recyclerView.adapter =
        PermissionsAdapter(
            items,
            itemListener
        )
}

@BindingAdapter(
    "app:favoritesRecyclerItems", "app:favoritesItemListener",
    requireAll = true
)
fun setFavoritesRecyclerViewItems(
    recyclerView: RecyclerView,
    viewData: FavoritesViewData?,
    itemListener: FavoritesListener?
) {
    val items = viewData?.items

    if (items.isNullOrEmpty()) {
        recyclerView.adapter = null
        return
    }

    recyclerView.adapter =
        FavoritesAdapter(
            items,
            itemListener
        )
}

@BindingAdapter(
    "app:userLocation", "app:mapViewData", "app:articleMarkerListener",
    requireAll = false
)
fun setMapViewData(
    mapView: MapView,
    userLocation: Pair<Double, Double>?,
    mapViewData: MapViewData?,
    articleMarkerListener: ArticlesOverlayListener.ArticleMarkerListener?
) {
    mapView.overlays.clear()

    val context = mapView.context
    mapViewData?.let { data ->
        val markersOverlay: ItemizedOverlayWithFocus<ArticlesOverlayItem> =
            ItemizedOverlayWithFocus(
                data.articlesOverlayData.map { it.toOverlayItem() },
                context.getDrawable(R.drawable.ic_location_marker),
                context.getDrawable(R.drawable.ic_location_marker_focused),
                context.resources.getColor(R.color.colorPrimary),
                ArticlesOverlayListener(mapView, articleMarkerListener),
                context
            )
        mapView.overlays.add(markersOverlay)
    }

    userLocation?.let {
        val userLocationOverlay = IconOverlay(
            it.toGeoPoint(),
            context.getDrawable(R.drawable.ic_my_location)
        )
        mapView.overlays.add(userLocationOverlay)
    }

    val copyrightOverlay = CopyrightOverlay(context)
    mapView.overlays.add(copyrightOverlay)

    mapView.invalidate()
}

@BindingAdapter("app:onZoomLevelChangedListener")
fun setZoomLevelListener(mapView: MapView, onZoomLevelChangedListener: ZoomLevelListener.OnZoomLevelChangedListener) {
    mapView.addMapListener(ZoomLevelListener(0.1, onZoomLevelChangedListener))
}