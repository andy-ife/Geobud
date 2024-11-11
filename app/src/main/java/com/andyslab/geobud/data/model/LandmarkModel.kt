package com.andyslab.geobud.data.model

data class LandmarkModel(
    val name: String,
    val country: String,
    val continent: String,
    val city: String = "",
    val funFact: String = "Lorem ipsum delerat sancti du poltice ard lakdum" +
                            " to faunti du placitus op sanctum lori" +
                            " alter cambrozi duparte al di sanctera falter" +
                            " unp",
    val photoUrl: String? = null,
    val photographer: String? = null,
    val photographerUrl: String? = null,
    )
