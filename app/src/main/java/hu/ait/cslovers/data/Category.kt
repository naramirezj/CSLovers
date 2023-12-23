package hu.ait.cslovers.data


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Category(
    @SerialName("name")
    var name: String? = null
)