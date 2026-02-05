package com.royalit.mvr.Model

import com.google.gson.annotations.SerializedName

data class AboutusResponse(
    @SerializedName("status"  ) var status  : String?    = null,
    @SerializedName("data") val data: DataAboutusResponse? = null
)

data class DataAboutusResponse(
    @SerializedName("about_us"  ) var about_us  : String?    = null,
    @SerializedName("mission" ) var mission : String? = null,
    @SerializedName("vision" ) var vision : String? = null
)