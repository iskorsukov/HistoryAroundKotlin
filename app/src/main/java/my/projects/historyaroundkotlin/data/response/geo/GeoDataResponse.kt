package my.projects.historyaroundkotlin.data.response.geo

import com.google.gson.annotations.SerializedName

data class GeoDataResponse(@SerializedName("geosearch") val data: List<GeoItemResponse>)