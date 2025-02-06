package com.royalit.mvr.Model

import com.google.gson.annotations.SerializedName

data class TermsAndConditionsResponse(
    @SerializedName("status"  ) var status  : String?    = null,
    @SerializedName("data") val data: DataTermsResponse? = null
)

data class DataTermsResponse(
    @SerializedName("content"  ) var content  : String?    = null
)
