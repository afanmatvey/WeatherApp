package com.konradszewczuk.weatherapp.data.room

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import io.reactivex.Flowable

@Dao
interface WeatherCitiesDao {

    @Insert
    fun insertAll(cities: List<CityEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCity(city: CityEntity)

    @Query(RoomConfig.SELECT_CITIES)
    fun getAllCities(): Flowable<List<CityEntity>>
}