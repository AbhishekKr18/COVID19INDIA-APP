package com.AbhishekKr.covid19India

import com.google.gson.annotations.SerializedName

data class StatesWiseData(
        @SerializedName("active") val activeCases : Int,
        @SerializedName("confirmed") val confirmedTotalCases: Int,
        val deaths : Int,
        val state: String
)