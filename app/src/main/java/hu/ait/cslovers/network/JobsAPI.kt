package hu.ait.cslovers.network

import hu.ait.cslovers.data.JobResult
import retrofit2.http.GET
import retrofit2.http.Query
import hu.ait.cslovers.data.Result
import retrofit2.http.Path

//https://www.themuse.com/api/public/jobs?category=Science%20and%20Engineering&location=United%20States&page=1
interface JobsAPI {
    @GET("api/public/jobs")
    suspend fun getJobs(
        @Query("category") category: String,
        @Query("level") level: String,
        @Query("location") location: String,
        @Query("page") page: String): JobResult


    @GET("api/public/jobs/{id}")
    suspend fun getSingleJob(
        @Path("id") id: String
    ): Result
}
