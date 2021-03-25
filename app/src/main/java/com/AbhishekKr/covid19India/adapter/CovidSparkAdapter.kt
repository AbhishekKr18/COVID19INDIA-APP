package com.AbhishekKr.covid19India.adapter

import android.graphics.RectF
import com.AbhishekKr.covid19India.CasesTimeSeries
import com.robinhood.spark.SparkAdapter

class CovidSparkAdapter(private val dailyData: List<CasesTimeSeries>) : SparkAdapter() {

    var metric = Metric.POSITIVE
    var daysAgo = TimeScale.MAX

    override fun getY(index: Int): Float {
        val chosenDayData = dailyData[index]
        return when(metric)
        {
            Metric.RECOVERED -> chosenDayData.dailyRecoveredCases.toFloat()
            Metric.POSITIVE -> chosenDayData.dailyPositiveCases.toFloat()
            Metric.DEATH -> chosenDayData.dailyDeaths.toFloat()
        }
    }

    override fun getItem(index: Int) = dailyData[index]

    override fun getCount() =  dailyData.size

    override fun getDataBounds(): RectF {
        //update the no. of days data shown in the chart for month week max

        val bounds = super.getDataBounds()
        if(daysAgo!=TimeScale.MAX) {
            bounds.left = count - daysAgo.numDays.toFloat()
        }
        return bounds
    }

}
