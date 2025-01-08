package pt.iade.planit.api

import android.content.Context
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import pt.iade.planit.R

object GeocodingHelper {
    private const val TAG = "GeocodingHelper"
    private const val BASE_URL = "https://maps.googleapis.com/maps/api/geocode/json"

    suspend fun getCoordinates(context: Context, address: String): Pair<Double, Double>? {
        val apiKey = context.getString(R.string.google_maps_key)
        val url = "$BASE_URL?address=${address.replace(" ", "+")}&key=$apiKey"

        return withContext(Dispatchers.IO) {
            try {
                Log.d(TAG, "Request URL: $url")
                val client = OkHttpClient()
                val request = Request.Builder()
                    .url(url)
                    .build()

                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()
                Log.d(TAG, "Response Body: $responseBody")

                if (response.isSuccessful && responseBody != null) {
                    val json = JSONObject(responseBody)
                    val results = json.getJSONArray("results")
                    if (results.length() > 0) {
                        val location = results.getJSONObject(0)
                            .getJSONObject("geometry")
                            .getJSONObject("location")
                        val lat = location.getDouble("lat")
                        val lng = location.getDouble("lng")
                        Log.d(TAG, "Coordinates: lat=$lat, lng=$lng")
                        return@withContext Pair(lat, lng)
                    } else {
                        Log.e(TAG, "No results found for the address")
                    }
                } else {
                    Log.e(TAG, "Response unsuccessful or empty")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching coordinates: ${e.message}", e)
            }
            null
        }
    }
}

    private fun getApiKey(context: Context): String? {
        val metaData = context.packageManager.getApplicationInfo(
            context.packageName,
            android.content.pm.PackageManager.GET_META_DATA
        ).metaData
        return metaData?.getString("com.google.android.maps.v2.API_KEY")
    }

