package com.royalit.mvr.Model

import com.google.gson.annotations.SerializedName

data class TransactionsModel(
    @SerializedName("status") var status: String? = null,
    @SerializedName("data") var data: List<TransactionsData>? = null
)

data class TransactionsData(
    @SerializedName("id") var id: String? = null,
    @SerializedName("project_id") var projectId: String? = null,
    @SerializedName("plot_no") var plotNo: String? = null,
    @SerializedName("amount") var amount: String? = null,
    @SerializedName("date") var date: String? = null,
    @SerializedName("employee_id") var employeeId: String? = null,
    @SerializedName("created_by") var createdBy: String? = null,
    @SerializedName("updated_by") var updatedBy: String? = null,
    @SerializedName("status") var status: String? = null,
    @SerializedName("created_at") var createdAt: String? = null,
    @SerializedName("updated_at") var updatedAt: String? = null,
    @SerializedName("employee_name") var employeeName: String? = null,
    @SerializedName("project_name") var projectName: String? = null,
    @SerializedName("notes") var notes: String? = null
)