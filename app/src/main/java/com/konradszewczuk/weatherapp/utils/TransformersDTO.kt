package com.konradszewczuk.weatherapp.utils

import com.konradszewczuk.weatherapp.data.remote.weatherModel.WeatherResponse
import com.konradszewczuk.weatherapp.domain.dto.HourlyWeatherDTO
import com.konradszewczuk.weatherapp.domain.dto.WeatherDetailsDTO
import com.konradszewczuk.weatherapp.domain.dto.WeeklyWeatherDTO
import java.util.ArrayList
import java.util.Date

object TransformersDTO {

    fun transformToWeatherDetailsDTO(cityName: String, weatherResponse: WeatherResponse?): WeatherDetailsDTO {
        val temperatureFahrenheit: Double? = weatherResponse?.currently?.temperature
        val temperature = WeatherMathUtils.convertFahrenheitToCelsius(temperatureFahrenheit)
        val cloudCoverPercentage: Double? = weatherResponse?.currently?.cloudCover
        val weatherSummary = weatherResponse?.currently?.summary
        val windSpeed = weatherResponse?.currently?.windSpeed
        val humidity = weatherResponse?.currently?.humidity

        val weeklyWeatherList = ArrayList<WeeklyWeatherDTO>()
        weatherResponse?.daily?.data?.forEach {
            if (it.time.toLong() * 1000 > Date().time)
                weeklyWeatherList.add(WeeklyWeatherDTO(it.temperatureMax.toString(), it.temperatureMin.toString(), StringFormatter.convertTimestampToDayOfTheWeek(it.time), it.icon))
        }

        val hourlyWeatherList = ArrayList<HourlyWeatherDTO>()
        weatherResponse?.hourly?.data?.forEach {
            hourlyWeatherList.add(HourlyWeatherDTO(it.time.toLong(), it.temperature))
        }

        var hourlyWeatherStringFormattedHoursList = ArrayList<String>()

        //temperature for only next 24hours
        if (hourlyWeatherList.size > 24) {
            hourlyWeatherStringFormattedHoursList = (0..24).mapTo(ArrayList()) {
                StringFormatter.convertTimestampToHourFormat(timestamp = hourlyWeatherList[it].timestamp, timeZone = weatherResponse?.timezone)
            }
        }

        return WeatherDetailsDTO(
            cityName = cityName,
            weatherSummary = weatherSummary,
            temperature = temperature,
            windSpeed = windSpeed,
            humidity = humidity?.let { it * 100 },
            cloudsPercentage = cloudCoverPercentage?.let { it * 100 },
            weeklyDayWeatherList = weeklyWeatherList,
            hourlyWeatherList = hourlyWeatherList,
            hourlyWeatherStringFormattedHoursList = hourlyWeatherStringFormattedHoursList
        )
    }
}