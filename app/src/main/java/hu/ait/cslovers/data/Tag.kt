package hu.ait.cslovers.data


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Tag(
    @SerialName("name")
    var name: String? = null,
    @SerialName("short_name")
    var shortName: String? = null
)