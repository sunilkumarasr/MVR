package com.royalit.mvr.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.royalit.mvr.Model.Amenity
import com.royalit.mvr.Model.GalleryImage
import com.royalit.mvr.R
import com.royalit.mvr.Retrofit.RetrofitClient

class ProjectGalleryAdapter(
    private val items: List<GalleryImage>,
    private val onItemClick: (GalleryImage) -> Unit // Click listener function
) : RecyclerView.Adapter<ProjectGalleryAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtImage: ImageView = itemView.findViewById(R.id.txtImage)

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
            .inflate(R.layout.item_project_gallery, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        Glide.with(holder.itemView.context)  // Use the context from itemView
            .load(RetrofitClient.Image_Path + item.image)  // Image URL
            .into(holder.txtImage)
    }

    override fun getItemCount(): Int {
        return items.size
    }
}