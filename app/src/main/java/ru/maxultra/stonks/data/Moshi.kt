package ru.maxultra.stonks.data

import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import ru.maxultra.stonks.data.model.ChartPoint

// Common Moshi instance
val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

// JSON chart conversion
private val type = Types.newParameterizedType(List::class.java, ChartPoint::class.java)
val adapter = moshi.adapter<List<ChartPoint>>(type)
