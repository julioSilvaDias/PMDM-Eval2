package com.example.ejercicioroom.logica.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

class Canciones {
    @Entity(tableName = "canciones")
    data class Canciones (
        @PrimaryKey(autoGenerate = true) val id: Int = 0,
        val titulo: String,
        val autor: String,
        val URL: String
    )
}

