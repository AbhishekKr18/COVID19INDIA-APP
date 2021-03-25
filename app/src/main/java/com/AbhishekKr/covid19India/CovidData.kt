package com.AbhishekKr.covid19India

data class CovidData(
         val state: String,
         val districtData: List<DistrictAndCases>
)