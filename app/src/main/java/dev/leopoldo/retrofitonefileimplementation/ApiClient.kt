package dev.leopoldo.retrofitonefileimplementation

import com.google.gson.annotations.SerializedName
import io.reactivex.Observable
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.util.concurrent.TimeUnit

class ApiClient {

    interface ApiInterface {
        @GET("random.php")
        fun getRandomDrink(): Observable<Drinks>
    }

    companion object {
        private const val BASE_URL = "https://www.thecocktaildb.com/api/json/v1/1/"

        private val apiClient by lazy { createApiClient() }

        private fun createApiClient(): Retrofit {

            val okHttpClient = OkHttpClient().newBuilder()
                .connectTimeout(20, TimeUnit.MINUTES)
                .writeTimeout(20, TimeUnit.MINUTES)
                .readTimeout(20, TimeUnit.MINUTES)
                .build()

            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()

        }

        fun getRandomDrink(): Observable<Drink> {
            return apiClient.create(ApiInterface::class.java).getRandomDrink().map {
                it.drinks.first()
            }
        }
    }

    data class Drinks(val drinks: List<Drink>)
    data class Drink(
        @SerializedName("idDrink") val id: String,
        @SerializedName("strDrink") val name: String
    )
}