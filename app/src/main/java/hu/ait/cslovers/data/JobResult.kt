package hu.ait.cslovers.data


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class JobResult(
    @SerialName("aggregations")
    var aggregations: Aggregations? = null,
    @SerialName("items_per_page")
    var itemsPerPage: Int? = null,
    @SerialName("page")
    var page: Int? = null,
    @SerialName("page_count")
    var pageCount: Int? = null,
    @SerialName("results")
    var results: List<Result?>? = null,
    @SerialName("timed_out")
    var timedOut: Boolean? = null,
    @SerialName("took")
    var took: Int? = null,
    @SerialName("total")
    var total: Int? = null
)