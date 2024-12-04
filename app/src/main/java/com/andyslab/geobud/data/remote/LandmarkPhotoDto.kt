package com.andyslab.geobud.data.remote

// Class representing a JSON response from pexels API.
data class LandmarkPhotoDto(
    val next_page: String,
    val page: Int,
    val per_page: Int,
    val photos: List<PhotoDto>,
    val total_results: Int,
)

data class PhotoDto(
    val alt: String,
    val avg_color: String,
    val height: Int,
    val id: Int,
    val liked: Boolean,
    val photographer: String,
    val photographer_id: Int,
    val photographer_url: String,
    val src: SrcDto,
    val url: String,
    val width: Int,
)

data class SrcDto(
    val landscape: String,
    val large: String,
    val large2x: String,
    val medium: String,
    val original: String,
    val portrait: String,
    val small: String,
    val tiny: String,
)
