package hu.ait.cslovers.data


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Refs(
    @SerialName("landing_page")
    var landingPage: String? = null
)