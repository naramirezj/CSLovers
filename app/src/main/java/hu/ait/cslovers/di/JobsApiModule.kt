package hu.ait.cslovers.di

import android.util.Log
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import hu.ait.cslovers.network.JobsAPI
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import java.io.IOException
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object  JobsApiModule{
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        val client = OkHttpClient.Builder()
            .addInterceptor(LoggingInterceptor()).build()

        return Retrofit.Builder()
            .baseUrl("https://www.themuse.com/")
            .addConverterFactory(Json { ignoreUnknownKeys = true }.asConverterFactory("application/json".toMediaType()) )
            .client(client)
            .build()

    }

    @Provides
    @Singleton
    fun provideJobsAPI(retrofit: Retrofit): JobsAPI {
        return retrofit.create(JobsAPI::class.java)
    }

}


class LoggingInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val request: Request = chain.request()
        val t1 = System.nanoTime()
        Log.d("TAG_HTTP", java.lang.String.format(
            "Sending request %s on %s%n%s",
            request.url, chain.connection(), request.headers))
        val response: okhttp3.Response = chain.proceed(request)
        val t2 = System.nanoTime()
        Log.d("TAG_HTTP", java.lang.String.format("Received response for %s in %.1fms%n%s",
            response.request.url, (t2 - t1) / 1e6, response.headers))
        return response
    }
}