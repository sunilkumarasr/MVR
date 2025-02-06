package com.royalit.mvr.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.royalit.mvr.Model.MyTeamsData
import com.royalit.mvr.R

class MyTeamsAdapter(
    private val items: List<MyTeamsData>,
    private val onItemClick: (MyTeamsData) -> Unit // Click listener function
) : RecyclerView.Adapter<MyTeamsAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtName: TextView = itemView.findViewById(R.id.txtName)
        val txtID: TextView = itemView.findViewById(R.id.txtID)
        val txtDesignation: TextView = itemView.findViewById(R.id.txtDesignation)
        val txtCity: TextView = itemView.findViewById(R.id.txtCity)
        val txtMobile: TextView = itemView.findViewById(R.id.txtMobile)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick(items[position])
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_team, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.txtName.text = item.name
        holder.txtID.text = item.id
        holder.txtDesignation.text = item.designation
        holder.txtCity.text = item.city
        holder.txtMobile.text = item.phone

    }

    override fun getItemCount(): Int {
        return items.size
    }
}