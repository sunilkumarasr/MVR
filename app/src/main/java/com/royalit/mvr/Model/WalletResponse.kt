package com.royalit.mvr.Model

import com.google.gson.annotations.SerializedName

data class WalletResponse(
    @SerializedName("status"  ) var status  : String?    = null,
    @SerializedName("message"  ) var message  : String?    = null,
    @SerializedName("balance"  ) var balance  : String?    = null
)
