package com.AbhishekKr.covid19India

data class DistrictAndCases(
        val district : String,
        val active : Int,
        val confirmed : Int,
        val deceased : Int
)