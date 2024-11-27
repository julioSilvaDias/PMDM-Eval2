package com.example.ejercicioroom

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.ejercicioroom.logica.bbdd.MyRoomDataBase
import com.example.ejercicioroom.logica.entity.Canciones
import com.example.myapplication.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity2 : AppCompatActivity() {
    private lateinit var editTitulo: EditText
    private lateinit var editAutor: EditText
    private lateinit var editUrl: EditText
    private lateinit var btnOk: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        // Inicializa las vistas
        editTitulo = findViewById(R.id.editTitulo)
        editAutor = findViewById(R.id.editAutor)
        editUrl = findViewById(R.id.editUrl)
        btnOk = findViewById(R.id.btnOk) // Asegúrate de inicializar btnOk aquí

        // Verifica si se está editando
        val isEditing = intent.getBooleanExtra("isEditing", false)

        if (isEditing) {
            // Rellenar datos existentes si estamos en modo edición
            val id = intent.getIntExtra("id", -1)
            val titulo = intent.getStringExtra("titulo")
            val autor = intent.getStringExtra("autor")
            val url = intent.getStringExtra("url")

            if (id != -1) {
                findViewById<EditText>(R.id.editId).setText(id.toString())
                editTitulo.setText(titulo)
                editAutor.setText(autor)
                editUrl.setText(url)
            }

            // Configura el botón para actualizar la canción
            btnOk.setOnClickListener {
                val updatedTitulo = editTitulo.text.toString()
                val updatedAutor = editAutor.text.toString()
                val updatedURL = editUrl.text.toString()

                if (updatedTitulo.isNotEmpty() && updatedAutor.isNotEmpty() && updatedURL.isNotEmpty()) {
                    val updatedCancion = Canciones(id, updatedTitulo, updatedAutor, updatedURL)
                    lifecycleScope.launch {
                        withContext(Dispatchers.IO) {
                            MyRoomDataBase.invoke(applicationContext).cancionesDao().update(updatedCancion)
                        }
                        setResult(Activity.RESULT_OK)
                        finish()
                    }
                } else {
                    Toast.makeText(this@MainActivity2, "Por favor, llena todos los campos.", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            // Configura el botón para agregar una nueva canción
            btnOk.setOnClickListener {
                val titulo = editTitulo.text.toString()
                val autor = editAutor.text.toString()
                val url = editUrl.text.toString()

                if (titulo.isNotEmpty() && autor.isNotEmpty() && url.isNotEmpty()) {
                    addCancion(titulo, autor, url)
                } else {
                    Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun addCancion(titulo: String, autor: String, url: String) {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                val dao = MyRoomDataBase.invoke(applicationContext).cancionesDao()
                dao.insert(Canciones(titulo = titulo, autor = autor, URL = url))
            }

            runOnUiThread {
                Toast.makeText(this@MainActivity2, "Canción añadida con éxito", Toast.LENGTH_SHORT).show()
                setResult(Activity.RESULT_OK)
                finish()
            }
        }
    }
}
