package com.royalit.mvr.Model

import com.google.gson.annotations.SerializedName

data class FaqModel(
    val status: String,
    val data: List<FaqModelData>?
)

data class FaqModelData(
    val id: String,
    val title: String,
    val slug: String,
    val description: String,
    val created_by: String,
    val status: String,
    var isExpanded: Boolean = false,
)
