package com.royalit.mvr.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.royalit.mvr.Model.Amenity
import com.royalit.mvr.R

class ProjectAmenitiesAdapter(
    private val items: List<Amenity>,
    private val onItemClick: (Amenity) -> Unit // Click listener function
) : RecyclerView.Adapter<ProjectAmenitiesAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtName: TextView = itemView.findViewById(R.id.txtName)
        val img: ImageView = itemView.findViewById(R.id.img)

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
            .inflate(R.layout.item_project_aminities, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.txtName.text = item.amenity
//        Glide.with(holder.itemView.context)  // Use the context from itemView
//            .load(RetrofitClient.Image_Path + item.image)  // Image URL
//            .into(holder.img)
    }

    override fun getItemCount(): Int {
        return items.size
    }
}