package com.example.ejercicioroom.logica.bbdd

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.ejercicioroom.logica.DAO.CancionesDAO
import com.example.ejercicioroom.logica.entity.Canciones

@Database (entities = [Canciones::class], version = 1)
abstract class MyRoomDataBase: RoomDatabase (){

    companion object{
        @Volatile private var instance : MyRoomDataBase? = null
        private val LOCK = Any()

        operator fun  invoke (context: Context) = instance?: synchronized(LOCK){
            instance?: buildDatabase (context).also {instance = it}
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(context,
            MyRoomDataBase::class.java, "myDataBase").build()
    }

    abstract fun cancionesDao() : CancionesDAO

}