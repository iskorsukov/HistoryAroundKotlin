package com.iskorsukov.historyaround.data.response.geo

import com.google.gson.annotations.SerializedName

data class GeoDataResponse(@SerializedName("geosearch") val data: List<GeoItemResponse>)