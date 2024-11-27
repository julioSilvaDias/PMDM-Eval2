package com.example.ejercicioroom

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.ejercicioroom.logica.bbdd.MyRoomDataBase
import com.example.ejercicioroom.logica.entity.Canciones
import com.example.myapplication.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var cancionesAdapter: CancionesAdapter
    private lateinit var listView: ListView
    val REQUEST_ADD_SONG = 1
    private var ismodoBorrado = false
    private var ismodoModificado = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listView = findViewById(R.id.listView)
        cancionesAdapter = CancionesAdapter(this, emptyList())
        listView.adapter = cancionesAdapter

        findViewById<Button>(R.id.btnAnadir).setOnClickListener {
            val intent = Intent(this, MainActivity2::class.java)
            startActivityForResult(intent, REQUEST_ADD_SONG)
        }

        val btnBorrar = findViewById<Button>(R.id.btnBorrar)

        btnBorrar.setOnClickListener {
            if(ismodoModificado){
                Toast.makeText(
                    this@MainActivity,
                    "No puedes borrar mientras estas modificando.",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            cambiarModoBorrado(btnBorrar)
        }

        findViewById<Button>(R.id.btnModificar).apply {
            setOnClickListener {
                if (ismodoBorrado) {
                    Toast.makeText(
                        this@MainActivity,
                        "No puedes modificar mientras estas en modo borrado.",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }

                ismodoModificado = !ismodoModificado
                text = if (ismodoModificado) "Cancelar Modificar" else "Modificar"
                configLisView()
            }
        }

        listView.setOnItemClickListener() { parent, view, position, id ->

            val cancionSeleccionada = cancionesAdapter.getItem(position)

            cancionSeleccionada?.let {
                reproducir(it.URL)
            }

        }
        configLisView()
        cargarCanciones()

    }

    private fun reproducir(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

    private fun cambiarModoBorrado(btnBorrar: Button) {
        ismodoBorrado = !ismodoBorrado
        if (ismodoBorrado) {
            btnBorrar.text = getString(R.string.btnTextBorrar)
        } else {
            btnBorrar.text = getString(R.string.btnTextReproducir)
        }

        configLisView()

    }

    private fun configLisView() {
        listView.setOnItemClickListener { parent, view, position, id ->
            val cancionSeleccionada = cancionesAdapter.getItem(position)
            cancionSeleccionada?.let {
                if (ismodoBorrado) {
                    eliminarCancion(it)
                } else if(ismodoModificado){
                    modificarCancion(it)
                }else{
                    reproducir(it.URL)
                }
            }
        }
    }

    private fun modificarCancion(cancion : Canciones){
        val intent = Intent(this, MainActivity2::class.java)
        intent.putExtra("isEditing", true)
        intent.putExtra("id", cancion.id)
        intent.putExtra("titulo", cancion.titulo)
        intent.putExtra("autor", cancion.autor)
        intent.putExtra("url", cancion.URL)
        startActivityForResult(intent, REQUEST_ADD_SONG)
    }

    private fun eliminarCancion(cancion: Canciones) {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                MyRoomDataBase.invoke(applicationContext).cancionesDao().delete(cancion)
                cargarCanciones()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_ADD_SONG && resultCode == RESULT_OK) {
            cargarCanciones()
        }
    }

    private fun cargarCanciones() {
        lifecycleScope.launch {
            val cancionesList = withContext(Dispatchers.IO) {
                MyRoomDataBase.invoke(applicationContext).cancionesDao().getAll()
            }
            cancionesAdapter.updateData(cancionesList)
        }
    }
}