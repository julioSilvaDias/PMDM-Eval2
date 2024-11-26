package com.example.ejercicioroom

import android.content.Context
import android.icu.text.Transliterator.Position
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.ejercicioroom.logica.entity.Canciones

class CancionesAdapter(
    private val context: Context,
    private var cancionesList: List<Canciones>
) : BaseAdapter() {

    override fun getCount(): Int {
        return cancionesList.size
    }

    override fun getItem(position: Int): Canciones {
        return cancionesList[position]
    }

    override fun getItemId(position: Int): Long {
        return cancionesList[position].id.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.item_cancion, parent, false)

        val tituloText = view.findViewById<TextView>(R.id.titulo)
        val autorText = view.findViewById<TextView>(R.id.autor)
        val urlText = view.findViewById<TextView>(R.id.URL)

        val cancion = getItem(position)

        tituloText.text = cancion.titulo
        autorText.text = cancion.autor
        urlText.text = cancion.URL

        return view
    }

    fun updateData(newList: List<Canciones>){
        cancionesList = newList
        notifyDataSetChanged()
    }

}
