package com.example.ejercicioroom.logica.DAO

import androidx.room.Query
import androidx.room.Room
import com.example.ejercicioroom.logica.entity.Canciones

interface CancionesDAO {
    @Query ("SELECT * FROM canciones ORDER BY titulo")
    fun getAll() : List<Canciones>

}
