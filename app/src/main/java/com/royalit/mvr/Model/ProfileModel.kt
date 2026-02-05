package com.royalit.mvr.Model

import com.google.gson.annotations.SerializedName

data class ProfileModel(
    @SerializedName("status"  ) var status  : String?    = null,
    @SerializedName("message" ) var message : String? = null,
    @SerializedName("data") val user: UserProfile? = null
)

data class UserProfile(
    @SerializedName("id") var id: String? = null,
    @SerializedName("emp_id") var empId: String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("phone") var phone: String? = null,
    @SerializedName("refferal_id") var referralId: String? = null,
    @SerializedName("designation") var designation: String? = null,
    @SerializedName("address") var address: String? = null,
    @SerializedName("city") var city: String? = null,
    @SerializedName("image") var image: String? = null,
    @SerializedName("email") var email: String? = null,
    @SerializedName("password") var password: String? = null,
    @SerializedName("created_at") var createdAt: String? = null,
    @SerializedName("status") var status: String? = null,
    @SerializedName("otp") var otp: String? = null,
    @SerializedName("created_by") var createdBy: String? = null,
    @SerializedName("updated_at") var updatedAt: String? = null,
    @SerializedName("updated_by") var updatedBy: String? = null
)
