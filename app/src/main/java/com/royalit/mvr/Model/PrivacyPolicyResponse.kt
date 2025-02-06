package com.royalit.mvr.Model

import com.google.gson.annotations.SerializedName

data class PrivacyPolicyResponse(
    @SerializedName("status"  ) var status  : String?    = null,
    @SerializedName("data") val data: DataprivacyResponse? = null
)

data class DataprivacyResponse(
    @SerializedName("content"  ) var content  : String?    = null
)
