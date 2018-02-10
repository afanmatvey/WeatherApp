package com.konradszewczuk.weatherapp.data.room

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context

@Database(entities = [CityEntity::class], version = 1)
abstract class RoomDataSource : RoomDatabase() {

    abstract fun weatherSearchCityDao(): WeatherCitiesDao

    companion object {

        @Volatile private var INSTANCE: RoomDataSource? = null

        fun getInstance(context: Context): RoomDataSource {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }
        }

        private fun buildDatabase(context: Context): RoomDataSource {
            val applicationContext = context.applicationContext
            val clazz = RoomDataSource::class.java

            return Room.databaseBuilder(applicationContext, clazz, RoomConfig.DATABASE_WEATHER).build()
        }
    }
}