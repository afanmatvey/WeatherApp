package com.konradszewczuk.weatherapp.utils

object WeatherMathUtils {

    fun convertFahrenheitToCelsius(temperatureFahrenheit: Double?): Double? {
        return if (temperatureFahrenheit != null) {
            ((temperatureFahrenheit - 32) * 5) / 9
        } else {
            null
        }
    }
}