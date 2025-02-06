package com.royalit.mvr.Model

import com.google.gson.annotations.SerializedName

data class PlotsStatusResponse(
    @SerializedName("status"  ) var status  : String?    = null,
    @SerializedName("data") val user: PlotsStatusData? = null
)

data class PlotsStatusData(
    @SerializedName("total_plots"  ) var total_plots  : String?    = null,
    @SerializedName("available_plots" ) var available_plots : String? = null,
    @SerializedName("booked_plots" ) var booked_plots : String? = null
)
