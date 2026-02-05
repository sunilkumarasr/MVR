package com.royalit.mvr.Model

import com.google.gson.annotations.SerializedName

data class ProjectModel(
    val status: String,
    val data: List<ProjectData>?
)

data class ProjectData(
    @SerializedName("pid") var pid: String? = null,
    @SerializedName("project_name") val projectName: String? = null,
    @SerializedName("broucher") val broucher: String? = null,
    @SerializedName("about_project") val aboutProject: String? = null,
    @SerializedName("banner_image") val banner_image: String? = null,
    @SerializedName("plot_image") val plotImage: String? = null,
    @SerializedName("video") val video: String? = null,
    @SerializedName("map") val map: String? = null,
    @SerializedName("location") val location: String? = null,
    @SerializedName("project_status") val projectStatus: String? = null,
    @SerializedName("amenities") val amenities: List<Amenity> = emptyList(),
    @SerializedName("gallery") val gallery: List<GalleryImage> = emptyList(),
    @SerializedName("highlight") val highlight: List<Highlight> = emptyList()
)

data class Amenity(
    @SerializedName("image") var image: String? = null,
    @SerializedName("amenity") var amenity: String? = null,
)

data class GalleryImage(
    @SerializedName("image") var image: String? = null,
)

data class Highlight(
    @SerializedName("image") var image: String? = null,
    @SerializedName("highlight") var highlight: String? = null,
)
