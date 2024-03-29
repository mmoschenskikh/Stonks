package ru.maxultra.stonks.data.network

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.maxultra.stonks.FMP_API_KEY
import ru.maxultra.stonks.data.moshi

interface FmpService {
    @GET("dowjones_constituent")
    suspend fun getStocks(@Query("apikey") apiKey: String = FMP_API_KEY): List<NetworkListStock>

    @GET("profile/{ticker}")
    suspend fun getProfile(
        @Path("ticker") ticker: String,
        @Query("apikey") apiKey: String = FMP_API_KEY
    ): List<NetworkProfileStock>

    @GET("search")
    suspend fun search(
        @Query("query") query: String,
        @Query("limit") limit: Int = 50,
        @Query("apikey") apiKey: String = FMP_API_KEY
    ): List<NetworkListStock>

    @GET("actives")
    suspend fun getPopular(@Query("apikey") apiKey: String = FMP_API_KEY): List<PopularListStock>

    @GET("historical-chart/30min/{ticker}")
    suspend fun getThirtyMinChart(
        @Path("ticker") ticker: String,
        @Query("apikey") apiKey: String = FMP_API_KEY
    ): List<NetworkChartPoint>
}


object StonksNetwork {
    private const val BASE_URL = "https://financialmodelingprep.com/api/v3/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    val service = retrofit.create(FmpService::class.java)
}
