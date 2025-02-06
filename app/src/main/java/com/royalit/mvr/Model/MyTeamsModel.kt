package com.royalit.mvr.Model

data class MyTeamsModel(
    val status: String,
    val data: List<MyTeamsData>?
)

data class MyTeamsData(
    val id: String,
    val emp_id: String,
    val name: String,
    val phone: String,
    val refferal_id: String,
    val designation: String,
    val address: String,
    val city: String,
    val image: String,
    val email: String,
    val password: String,
    val created_at: String,
    val status: String,
    val otp: String,
    val created_by: String?,
    val updated_at: String?,
    val updated_by: String?
)
