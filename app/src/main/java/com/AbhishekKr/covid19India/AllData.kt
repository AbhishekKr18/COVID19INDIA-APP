package com.AbhishekKr.covid19India

import com.google.gson.annotations.SerializedName

data class AllData(
        @SerializedName("cases_time_series") val perDayCases : List<CasesTimeSeries>,

        @SerializedName("statewise") val totalStateWise : List<StatesWiseData>
)