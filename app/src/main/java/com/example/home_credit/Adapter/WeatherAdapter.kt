package com.example.home_credit.Adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.home_credit.Adapter.WeatherAdapter.WeatherAdapterViewHolder
import com.example.home_credit.Interfaces.OnItemClickListener
import com.example.home_credit.Model.WeatherModel
import com.example.home_credit.NextPageFragment
import com.example.home_credit.R
import java.util.*

class WeatherAdapter(private val context: Context, weatherModelList: List<WeatherModel>) :
    RecyclerView.Adapter<WeatherAdapterViewHolder>() {
    private var weatherModelList: List<WeatherModel> = ArrayList()



    // On Click Listener
    fun setOnItemClickListener(onItemClickListener: OnItemClickListener?) {
        Companion.onItemClickListener = onItemClickListener
    }

    // Initializing View
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherAdapterViewHolder {
        val rootView = LayoutInflater.from(parent.context).inflate(R.layout.cardview, parent, false)
        val evh = WeatherAdapterViewHolder(rootView, onItemClickListener)
        return evh
    }

    override fun onBindViewHolder(holder: WeatherAdapterViewHolder, position: Int) {
        val currentItem = weatherModelList[position]
        holder.mTemperature.text = currentItem.model_temperature + " °C"
        holder.mCity.text = currentItem.model_cityName
        holder.mWeather.text = currentItem.model_weatherType

        var card: CardView = holder.itemView.findViewById(R.id.card_view)

        val rnd = Random()
        var currentColor = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
        card.setBackgroundColor(currentColor)


        holder.itemView.setOnClickListener{ v->


            val activity: AppCompatActivity = v.getContext() as AppCompatActivity
            val myFragment: Fragment = NextPageFragment(
                currentItem.model_cityName,
                currentItem.model_temperature,
                currentItem.model_weatherType,
                currentItem.model_temp_min,
                currentItem.model_temp_max
            )

            activity.getSupportFragmentManager().beginTransaction().replace(
                R.id.fl_wrapper,
                myFragment
            ).addToBackStack(null).commit();


        }
    }

    // View Id's
    class WeatherAdapterViewHolder(itemView: View, onItemClickListener: OnItemClickListener?) : RecyclerView.ViewHolder(
        itemView
    ) {
        var mTemperature: TextView
        var mCity: TextView
        var mWeather: TextView

        init {
            mTemperature = itemView.findViewById(R.id.card_temp)
            mCity = itemView.findViewById(R.id.card_city)
            mWeather = itemView.findViewById(R.id.card_sky)

        }
    }

    // Get Item Count
    override fun getItemCount(): Int {
        return weatherModelList.size
    }

    companion object {
        var onItemClickListener: OnItemClickListener? = null
    }

    // Pass Context to view
    init {
        this.weatherModelList = weatherModelList
    }
}