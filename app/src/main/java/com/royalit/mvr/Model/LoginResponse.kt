package com.royalit.mvr.Model

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("status"  ) var status  : String?    = null,
    @SerializedName("message" ) var message : String? = null,
    @SerializedName("user") val user: User? = null
)

data class User(
    @SerializedName("id"  ) var id  : String?    = null,
    @SerializedName("name" ) var name : String? = null,
    @SerializedName("email" ) var email : String? = null,
    @SerializedName("phone" ) var phone : String? = null,
    @SerializedName("created_at" ) var created_at : String? = null
)

data class LoginRequest(
    val phone: String,
    val password: String
)
