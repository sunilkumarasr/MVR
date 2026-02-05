package com.royalit.mvr.Model

import com.google.gson.annotations.SerializedName

data class RegisterResponse(
    @SerializedName("status"  ) var status  : String?    = null,
    @SerializedName("message" ) var message : String? = null
)

data class RegisterRequest(
    val name: String,
    val phone: String,
    val email: String,
    val city: String,
    val address: String,
    val refferal_id: String,
    val password: String
)