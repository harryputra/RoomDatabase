package com.example.roomdatabase.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class SpinnerJurusanAdapter(
    context: Context,
    resource: Int,
    private val jurusanList: List<String>
) : ArrayAdapter<String>(context, resource, jurusanList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createView(position, convertView, parent)
    }

    private fun createView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(android.R.layout.simple_spinner_item, parent, false)
        val text = view.findViewById<TextView>(android.R.id.text1)
        text.text = jurusanList[position]
        return view
    }

    fun getPosition(item: String): Int {
        return jurusanList.indexOf(item)
    }
}