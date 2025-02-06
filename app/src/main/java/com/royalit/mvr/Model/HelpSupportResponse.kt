package com.royalit.mvr.Model

import com.google.gson.annotations.SerializedName

data class HelpSupportResponse(
    @SerializedName("status"  ) var status  : String?    = null,
    @SerializedName("message"  ) var message  : String?    = null
)
