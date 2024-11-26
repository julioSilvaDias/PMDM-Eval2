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

        val isEditing = intent.getBooleanExtra("isEditing", false)

        if(isEditing){
            val id = intent.getIntExtra("id", -1)
            val titulo = intent.getStringExtra("titulo")
            val autor = intent.getStringExtra("autor")
            val URL = intent.getStringExtra("URL")

            findViewById<EditText>(R.id.editId).setText(id.toString())
            findViewById<EditText>(R.id.editTitulo).setText(titulo)
            findViewById<EditText>(R.id.editAutor).setText(autor)
            findViewById<EditText>(R.id.editUrl).setText(URL)

            findViewById<Button>(R.id.btnOk).setOnClickListener {
                val updatedTitulo = findViewById<EditText>(R.id.editTitulo).text.toString()
                val updatedAutor = findViewById<EditText>(R.id.editAutor).text.toString()
                val updatedURL = findViewById<EditText>(R.id.editUrl).text.toString()

                if(updatedTitulo.isNotEmpty() && updatedAutor.isNotEmpty() && updatedURL.isNotEmpty()){
                    val updatedCacion = Canciones(id, updatedTitulo, updatedAutor, updatedURL)
                    lifecycleScope.launch {
                        withContext(Dispatchers.IO){
                            MyRoomDataBase.invoke(applicationContext).cancionesDao().update(updatedCacion)
                        }

                        setResult(Activity.RESULT_OK)
                        finish()
                    }

                }else{
                    Toast.makeText(this@MainActivity2, "Por favor, llena todos los campos.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        editTitulo = findViewById(R.id.editTitulo)
        editAutor = findViewById(R.id.editAutor)
        editUrl = findViewById(R.id.editUrl)

        btnOk.setOnClickListener {
            val titulo = editTitulo.text.toString()
            val autor = editAutor.text.toString()
            val url = editUrl.text.toString()

            if(titulo.isNotEmpty() && autor.isNotEmpty() && url.isNotEmpty()){
                addCancion(titulo, autor, url)
            }else{
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun addCancion(titulo: String, autor: String, url: String){
        lifecycleScope.launch {
            withContext(Dispatchers.IO){
                val dao = MyRoomDataBase.invoke(applicationContext).cancionesDao()
                dao.insert(Canciones(titulo = titulo, autor = autor, URL = url))
            }

            runOnUiThread{
                Toast.makeText(this@MainActivity2, "Cancion añadida con exito", Toast.LENGTH_SHORT).show()
                setResult(Activity.RESULT_OK)
                finish()
            }
        }

    }
}