// RetrofitInstance.kt
package pt.iade.planit.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    // Defina a URL base da sua API
    private const val BASE_URL = "http://10.0.2.2:8081"

    // Instância do Retrofit configurada
    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }).build())
            .build()
    }

    // Instância da ApiService para uso no aplicativo
    val api: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}
