package com.example.roomdatabase.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.roomdatabase.R
import com.example.roomdatabase.data.entity.User

class UserAdapter(private val context: Context, private val list: List<User>) : RecyclerView.Adapter<UserAdapter.ViewAdapter>() {
    private var dialog: Dialog? = null

    interface Dialog {
        fun onClick(position: Int)
    }

    fun setDialog(dialog: Dialog) {
        this.dialog = dialog
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewAdapter {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_user, parent, false)
        return ViewAdapter(view)
    }

    override fun onBindViewHolder(holder: ViewAdapter, position: Int) {
        holder.fullName.text = list[position].fullName
        holder.email.text = list[position].email
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewAdapter(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var fullName: TextView = itemView.findViewById(R.id.full_name)
        var email: TextView = itemView.findViewById(R.id.email)

        init {
            itemView.setOnClickListener {
                dialog?.onClick(layoutPosition)
            }
        }
    }
}