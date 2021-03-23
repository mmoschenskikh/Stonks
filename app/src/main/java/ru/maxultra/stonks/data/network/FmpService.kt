package ru.maxultra.stonks.data.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import ru.maxultra.stonks.FMP_API_KEY

interface FmpService {
    @GET("dowjones_constituent?apikey=${FMP_API_KEY}")
    suspend fun getStocks(): List<NetworkListStock>
}

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

object StonksNetwork {
    private const val BASE_URL = "https://financialmodelingprep.com/api/v3/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    val service = retrofit.create(FmpService::class.java)
}