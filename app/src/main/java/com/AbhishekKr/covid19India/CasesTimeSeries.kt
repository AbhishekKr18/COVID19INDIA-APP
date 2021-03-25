package com.AbhishekKr.covid19India

import com.google.gson.annotations.SerializedName
import java.util.*

data class CasesTimeSeries(
        @SerializedName("dateymd") val dateChecked: Date,
        @SerializedName("dailyconfirmed") val dailyPositiveCases: Int,
        @SerializedName("dailyrecovered") val dailyRecoveredCases: Int,
        @SerializedName("dailydeceased") val dailyDeaths: Int
)