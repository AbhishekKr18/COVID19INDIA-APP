package com.AbhishekKr.covid19India.adapter

enum class Metric{
    RECOVERED, POSITIVE, DEATH
}

enum class TimeScale(val numDays : Int){
    WEEK(7),
    MONTH(30),
    MAX(-1)
}