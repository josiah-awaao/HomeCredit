package com.example.home_credit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.home_credit.Model.WeatherModel

class NextPageFragment(
    city: String,
    temperature: String,
    weatherType: String,
    TempMin: String,
    TempMax: String
) : Fragment(){

    private lateinit var mWeatherCity: TextView
    private lateinit var mWeatherTemperature : TextView
    private lateinit var mWeatherPattern: TextView
    private lateinit var mWeatherHigh: TextView
    private lateinit var mWeatherLow: TextView

    private lateinit var weatherModel: MutableList<WeatherModel>

    var tempCity = city
    var tempTemperature = temperature
    var tempweatherType = weatherType
    var tempTempMin = TempMin
    var tempTempMax = TempMax


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.next_page_fragment, container, false)

        // Initialization
        mWeatherCity = rootView.findViewById(R.id.weather_City)
        mWeatherTemperature = rootView.findViewById(R.id.weather_Temp)
        mWeatherPattern= rootView.findViewById(R.id.weather_Pattern)
        mWeatherHigh = rootView.findViewById(R.id.weather_High)
        mWeatherLow = rootView.findViewById(R.id.weather_Low)


        mWeatherCity.text = tempCity
        mWeatherTemperature.text = tempTemperature
        mWeatherPattern.text = tempweatherType
        mWeatherLow.text = "Low $tempTempMin °C"
        mWeatherHigh.text = "High $tempTempMax °C"


        return rootView
    }

}