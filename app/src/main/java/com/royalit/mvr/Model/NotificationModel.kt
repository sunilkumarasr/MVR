package com.royalit.mvr.Model

data class NotificationModel(
    val status: String,
    val data: List<NotificationData>?
)

data class NotificationData(
    val id: String,
    val title: String,
    val body: String,
    val status: String,
    val created_at: String,
)
