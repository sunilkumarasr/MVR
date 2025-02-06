package com.royalit.mvr.Model

import java.io.Serializable

data class SaleModel(
    val status: String,
    val data: List<SaleData>?
)

data class SaleData(
    val id: String,
    val employee: String,
    val project_name: String,
    val plot_no: String,
    val description: String,
    val cost: String,
    val facing: String,
    val sale_amount: String,
    val Sale_status: String,
    val employee_commision: String,
    val date: String,
    val customer_name: String,
    val customer_mobile: String,
    val customer_email: String,
    val sold_square_yard: String,
    val total_amount: String,
    val created_at: String,
    val updated_at: String?,
    val updated_by: String?,
    val status: String,
    val employee_name: String
) : Serializable
