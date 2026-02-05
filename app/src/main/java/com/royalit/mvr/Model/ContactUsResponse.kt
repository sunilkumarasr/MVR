package com.royalit.mvr.Model

import com.google.gson.annotations.SerializedName

data class ContactUsResponse(
    @SerializedName("status"  ) var status  : String?    = null,
    @SerializedName("data") val data: DataContactUsResponse? = null
)

data class DataContactUsResponse(
    @SerializedName("id"  ) var id  : String?    = null,
    @SerializedName("location"  ) var location  : String?    = null,
    @SerializedName("mobile"  ) var mobile  : String?    = null,
    @SerializedName("mail"  ) var mail  : String?    = null
)