package com.example.home_credit

//import com.android.volley.Request
//import com.android.volley.toolbox.StringRequest
//import com.android.volley.toolbox.Volley
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.home_credit.Adapter.WeatherAdapter
import com.example.home_credit.Interfaces.OnItemClickListener
import com.example.home_credit.Model.WeatherModel
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException


class MainActivity : AppCompatActivity() {

    private lateinit var weatherAdapter: WeatherAdapter
    private lateinit var weatherModel: MutableList<WeatherModel>
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerViewLayoutManager: RecyclerView.LayoutManager

    private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout

    var city_array: Array<String> = arrayOf("Manila", "Prague", "Seoul")

    var mainWeatherPattern = ""



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        weatherModel = ArrayList()

        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.setHasFixedSize(true)

        recyclerViewLayoutManager = LinearLayoutManager(this)
        weatherAdapter = WeatherAdapter(this, weatherModel)

        recyclerView.layoutManager = recyclerViewLayoutManager
        recyclerView.adapter = weatherAdapter

        mSwipeRefreshLayout = findViewById(R.id.swipe_refresh_main)

        mSwipeRefreshLayout.setOnRefreshListener {
            if (mSwipeRefreshLayout.isRefreshing){
                weatherModel.clear()
                for(i in city_array) {
                    getWeather(i)
                }
                Toast.makeText(applicationContext, "Refreshed", Toast.LENGTH_SHORT).show()
            }
        }


        for(i in city_array) {
            getWeather(i)
        }

    }

    private fun getWeather(city_array: String) {
        val ACCESS_KEY = "917dae5939cd9b5091194b2f33d078bc"


        val url = "https://api.openweathermap.org/data/2.5/weather".toHttpUrlOrNull()!!.newBuilder()// Parse String of URL
            .addEncodedQueryParameter("q", city_array)
            .addEncodedQueryParameter("appid", ACCESS_KEY)
            .addEncodedQueryParameter("units", "metric")
            .build().toString()

        Log.d(
            "Build ",
            "https://api.openweathermap.org/data/2.5/weather".toHttpUrlOrNull()!!.newBuilder()
                .addEncodedQueryParameter(
                    "q",
                    "Manila"
                ).addEncodedQueryParameter("appid", ACCESS_KEY).build().toString()
        )

        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .get()
            .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                }

                override fun onResponse(call: Call, response: Response) {
                    val myResponse = response.body!!.string()
                    Log.d("Body ", myResponse)

                    if (response.isSuccessful) {
                        try {
                            val responseObj = JSONObject(myResponse)
                            val weather = responseObj.optString("weather")

                            val feedValues = JSONArray(weather)
                            for (i in 0 until feedValues.length()) {

                                val feedValuesObj = feedValues.getJSONObject(i)

                                val id = feedValuesObj.optInt("id")
                                val main = feedValuesObj.optString("main")
                                val description = feedValuesObj.optString("description")
                                val icon = feedValuesObj.optString("icon")
                                mainWeatherPattern = main

                            }

                            val main = responseObj.optString("main")
                            val itemsInMain = JSONObject(main)
                            val temp = itemsInMain.optString("temp")
                            val temp_max = itemsInMain.optString("temp_max")
                            val temp_min = itemsInMain.optString("temp_min")



                            val f: Float = java.lang.Float.valueOf(temp)
                            val newOneDec = String.format("%.1f", f)

                            val tempMax: Float = java.lang.Float.valueOf(temp_max)
                            val maxNoDec = String.format("%.0f", tempMax)

                            val tempMin: Float = java.lang.Float.valueOf(temp_min)
                            val minNoDec = String.format("%.0f", tempMin)


                            val name = responseObj.optString("name")


                            weatherModel.add(
                                WeatherModel(
                                    temp, name, mainWeatherPattern, temp_min, temp_max
                                )
                            )

                            runOnUiThread {
                                weatherAdapter = WeatherAdapter(applicationContext, weatherModel)
                                recyclerView.adapter = null
                                recyclerView.adapter = weatherAdapter
                            }

                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                        Log.d("Body ", "Successful!")
                        mSwipeRefreshLayout.isRefreshing = false
                    }
                }
            })
    }
}