package com.AbhishekKr.covid19India

import retrofit2.Call
import retrofit2.http.GET

interface CovidService {

    @GET("data.json")
    fun getNationalAndStateData(): Call<AllData>

    @GET("v2/state_district_wise.json")
    fun getStateDistrictData(): Call<List<CovidData>>

}