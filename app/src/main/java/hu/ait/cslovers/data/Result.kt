package hu.ait.cslovers.data


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Result(
    @SerialName("categories")
    var categories: List<Category?>? = null,
    @SerialName("company")
    var company: Company? = null,
    @SerialName("contents")
    var contents: String? = null,
    @SerialName("id")
    var id: Int? = null,
    @SerialName("levels")
    var levels: List<Level?>? = null,
    @SerialName("locations")
    var locations: List<Location?>? = null,
    @SerialName("model_type")
    var modelType: String? = null,
    @SerialName("name")
    var name: String? = null,
    @SerialName("publication_date")
    var publicationDate: String? = null,
    @SerialName("refs")
    var refs: Refs? = null,
    @SerialName("short_name")
    var shortName: String? = null,
    @SerialName("tags")
    var tags: List<Tag?>? = null,
    @SerialName("type")
    var type: String? = null
) : java.io.Serializable