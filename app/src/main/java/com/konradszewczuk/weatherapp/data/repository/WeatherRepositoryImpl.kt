package com.konradszewczuk.weatherapp.data.repository

import com.konradszewczuk.weatherapp.data.remote.RemoteWeatherDataSource
import com.konradszewczuk.weatherapp.data.room.CityEntity
import com.konradszewczuk.weatherapp.data.room.RoomDataSource
import com.konradszewczuk.weatherapp.domain.dto.WeatherDetailsDTO
import com.konradszewczuk.weatherapp.utils.TransformersDTO
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepositoryImpl @Inject constructor(
    private val remoteWeatherDataSource: RemoteWeatherDataSource,
    private val roomDataSource: RoomDataSource
) : WeatherRepository {

    override fun getWeather(cityName: String): Single<WeatherDetailsDTO> {
        return remoteWeatherDataSource.requestCityAddressByName(cityName)
            .map {
                val result = it.results.first()
                return@map Pair(result.geometry.location, result.formatted_address)
            }
            .flatMap { (location, address) ->
                return@flatMap remoteWeatherDataSource.requestWeatherForCity(location.lat.toString(), location.lng.toString())
                    .map { TransformersDTO.transformToWeatherDetailsDTO(address, it) }
            }
            .retry(1)
    }

    override fun getCities(): Flowable<List<CityEntity>> {
        return roomDataSource.weatherSearchCityDao().getAllCities()
    }

    override fun addCity(cityName: String) {
        Completable.fromCallable { roomDataSource.weatherSearchCityDao().insertCity(CityEntity(cityName = cityName)) }
            .subscribeOn(Schedulers.io())
            .subscribe()
    }

}