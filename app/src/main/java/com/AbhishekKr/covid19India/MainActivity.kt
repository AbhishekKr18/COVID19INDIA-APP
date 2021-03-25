package com.AbhishekKr.covid19India

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings.Global.putString
import android.provider.Settings.Secure.putString
import android.provider.Settings.System.putString
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import com.AbhishekKr.covid19India.adapter.CovidSparkAdapter
import com.AbhishekKr.covid19India.adapter.Metric
import com.AbhishekKr.covid19India.adapter.TimeScale
import com.google.gson.GsonBuilder
import com.robinhood.ticker.TickerUtils
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import com.AbhishekKr.covid19India.NATIONAL_DAILY_DATA as NATIONAL_DAILY_DATA
import kotlin.collections.groupBy as groupBy1

private const val BASE_URL = "https://api.covid19india.org/"
private const val TAG = "MainActivity"
private const val NATIONAL_DAILY_DATA = "National Daily Data"
class MainActivity : AppCompatActivity() {

    private lateinit var totalStateWiseData: Map<String, List<StatesWiseData>>
    private lateinit var currentlyShownData: List<CasesTimeSeries>
    private lateinit var adapter: CovidSparkAdapter
    private lateinit var nationalDailyData: List<CasesTimeSeries>
//    private lateinit var districtWiseData: Map<String, List<CovidData>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val gson = GsonBuilder().setDateFormat("yyyy-MM-dd").create()
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        val covidService = retrofit.create(CovidService::class.java)

        //fetch the daily national data
        covidService.getNationalAndStateData().enqueue(object : Callback<AllData> {
            override fun onFailure(call: Call<AllData>, t: Throwable) {
                Log.e(TAG, "onFailure $t")
            }

            override fun onResponse(call: Call<AllData>, response: Response<AllData>) {
                Log.i(TAG, "onResponse $response")
                val nationalData = response.body()?.perDayCases
//                val stateData = response.body()?.totalStateWise?.groupBy1 { it.state }
                if (nationalData == null) {
                    Log.w(TAG, "Did not receive a valid response body for nationaldailydata")
                    return
                }
                setupEventListeners()
                nationalDailyData = nationalData
                Log.i(TAG, "Update graph with national data")
                updateDisplayWithData(nationalDailyData)


//                if (stateData != null) {
//                    totalStateWiseData = stateData
//                }
            }

        })

        //fetch the district data
        //Just getting district data.No use in the app for this data so commented
//        covidService.getStateDistrictData().enqueue(object : Callback<List<CovidData>> {
//            override fun onFailure(call: Call<List<CovidData>>, t: Throwable) {
//                Log.e(TAG, "onFailure $t")
//            }
//
//            override fun onResponse(
//                call: Call<List<CovidData>>,
//                response: Response<List<CovidData>>
//            ) {
//                Log.i(TAG, "onResponse $response")
//                val districtsCases = response.body()
//                if (districtsCases == null) {
//                    Log.w(TAG, "Did not receive a valid response body")
//                    return
//
//                }
//                districtWiseData = districtsCases.groupBy1 { it.state }
//                Log.i(TAG, "show data in other activity")
//
//            }
//
//        })
    }

        fun setupEventListeners() {
        tickerView.setCharacterLists(TickerUtils.provideNumberList())
        //add a listener for the user scrubbing on the chart
        sparkView.isScrubEnabled = true
        sparkView.setScrubListener { itemData->
            if(itemData is CasesTimeSeries) {
                updateInfoForDate(itemData)
            }
        }
        //respond to radio button selected events
        radioGroupTimeSelection.setOnCheckedChangeListener { _, checkedId ->
            adapter.daysAgo = when(checkedId)
            {
                R.id.radioButtonWeek -> TimeScale.WEEK
                R.id.radioButtonMonth -> TimeScale.MONTH
                else -> TimeScale.MAX
            }
            adapter.notifyDataSetChanged()
        }

        radioGroupMetricSelection.setOnCheckedChangeListener { _, checkedId ->
            when(checkedId)
            {
                R.id.radioButtonRecovered -> updateDisplayMetric(Metric.RECOVERED)
                R.id.radioButtonPositive -> updateDisplayMetric(Metric.POSITIVE)
                R.id.radioButtonDeath -> updateDisplayMetric(Metric.DEATH)
            }
        }
    }

    private fun updateDisplayMetric(metric: Metric) {
        //update the color of chart lines
        val colorAccent = when(metric)
        {
            Metric.RECOVERED -> R.color.colorRecovered
            Metric.POSITIVE -> R.color.colorPositive
            Metric.DEATH -> R.color.colorDeaths
        }
        @ColorInt val colorInt = ContextCompat.getColor(this,colorAccent)
        sparkView.lineColor = colorInt
        tickerView.setTextColor(colorInt)


        adapter.metric = metric
        adapter.notifyDataSetChanged()

        //reset number and date shown in bottom text view
        updateInfoForDate(currentlyShownData.last())
    }

    private fun updateDisplayWithData(dailyData: List<CasesTimeSeries>) {
        currentlyShownData = dailyData
        //create a spark adapter to render data to spark chart
        adapter = CovidSparkAdapter(dailyData)
        sparkView.adapter = adapter
        //by default update the radio buttons to select positive and max options
        radioButtonPositive.isChecked = true
        radioButtonMax.isChecked = true
        //display the metric for the most recent date
        updateDisplayMetric(Metric.POSITIVE)
    }

    private fun updateInfoForDate(casesTimeSeries: CasesTimeSeries) {
        val numCases = when(adapter.metric)
        {
            Metric.RECOVERED -> casesTimeSeries.dailyRecoveredCases
            Metric.POSITIVE -> casesTimeSeries.dailyPositiveCases
            Metric.DEATH -> casesTimeSeries.dailyDeaths
        }
        //update two text view at the bottom of the screen with data inside of this parameter
        tickerView.text = NumberFormat.getInstance().format(numCases)
        val outputDateFormat = SimpleDateFormat("dd MMM, yyyy", Locale.ENGLISH)
        tvDateLabel.text = outputDateFormat.format(casesTimeSeries.dateChecked)

    }


}

