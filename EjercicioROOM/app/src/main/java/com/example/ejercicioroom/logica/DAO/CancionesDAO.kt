package com.example.ejercicioroom.logica.DAO

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Room
import androidx.room.Update
import com.example.ejercicioroom.logica.entity.Canciones

interface CancionesDAO {
    @Query ("SELECT * FROM canciones ORDER BY titulo")
    fun getAll() : List<Canciones>

    @Insert
    fun insert(cancion: Canciones)

    @Delete
    fun delete(cancion: Canciones)

    @Update
    fun update(cancion: Canciones)

}
