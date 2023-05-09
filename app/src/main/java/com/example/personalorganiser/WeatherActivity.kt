package com.example.personalorganiser

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class WeatherActivity : AppCompatActivity(), LocationListener{

    private lateinit var locationManager: LocationManager
    private lateinit var weatherTextView: TextView
    private var currentLocation: Location? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather_main)

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        weatherTextView = findViewById(R.id.weather)

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
            getCurrentLocation()
        }
    }

    companion object {
        const val LOCATION_PERMISSION_REQUEST_CODE = 123
        const val MIN_TIME_BW_UPDATES = 1000 * 60 * 1L // 1 minute
        const val MIN_DISTANCE_CHANGE_FOR_UPDATES = 10f // 10 meters
    }

    private fun getCurrentLocation() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Permission has been granted, proceed with location updates
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    MIN_TIME_BW_UPDATES,
                    MIN_DISTANCE_CHANGE_FOR_UPDATES,
                    this
                )
                val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                location?.let { fetchWeatherData(it) }
            }
        } else {
            // Permission has not been granted, request it
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE)
        }
    }


    override fun onLocationChanged(location: Location) {
        fetchWeatherData(location)
    }

    private fun fetchWeatherData(location: Location) {
        currentLocation = location  // Assign location to currentLocation variable
        val url =
            "https://api.openweathermap.org/data/2.5/weather?lat=${location.latitude}&lon=${location.longitude}&appid=4c3cc1e573add89f35647574bf5de756"
        FetchWeatherTask().execute(url)
    }



    @SuppressLint("StaticFieldLeak")
    inner class FetchWeatherTask : AsyncTask<String, Void, Double?>() {

        @Deprecated("Deprecated in Java")
        override fun doInBackground(vararg urls: String): Double? {
            val url: URL
            val connection: HttpURLConnection
            val builder = StringBuilder()

            try {
                url = URL(urls[0])
                connection = url.openConnection() as HttpURLConnection
                connection.connect()

                val reader = BufferedReader(InputStreamReader(connection.inputStream))
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    builder.append(line)
                }
                reader.close()

                val jsonObject = JSONObject(builder.toString())
                val main = jsonObject.getJSONObject("main")
                return main.getDouble("temp")
            } catch (e: IOException) {
                Log.e("WeatherTask", "Error connecting to OpenWeatherMap API", e)
            } catch (e: JSONException) {
                Log.e("WeatherTask", "Error parsing JSON", e)
            }

            return null
        }


        @Deprecated("Deprecated in Java")
        override fun onPostExecute(temperature: Double?) {
            if (temperature != null) {
                val geocoder = Geocoder(this@WeatherActivity)
                val addresses = currentLocation?.let { geocoder.getFromLocation(it.latitude, currentLocation!!.longitude, 1) }
                val locationName = addresses?.get(0)?.getAddressLine(0)
                val celsiusTemp = temperature - 273.15
                weatherTextView.text = String.format("Weather in %s: %.1f°C", locationName, celsiusTemp)

                val resultIntent = Intent()
                resultIntent.putExtra("EXTRA_WEATHER_API", celsiusTemp.toString())
                setResult(Activity.RESULT_OK, resultIntent)
                finish()


            }
        }




    }

    override fun onProviderEnabled(provider: String) {
    }

    override fun onProviderDisabled(provider: String) {
    }

    @Deprecated("Deprecated in Java")
    override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
    }

}
