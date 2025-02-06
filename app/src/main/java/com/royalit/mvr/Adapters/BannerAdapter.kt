package com.royalit.mvr.Adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.royalit.mvr.Model.ProjectData
import com.royalit.mvr.R
import com.royalit.mvr.Retrofit.RetrofitClient

class BannerAdapter(
    private val banners: List<ProjectData>,
    private val onItemClick: (ProjectData) -> Unit // Pass a lambda for item clicks
) : RecyclerView.Adapter<BannerAdapter.BannerViewHolder>() {

    inner class BannerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtName: TextView = itemView.findViewById(R.id.txtName)
        val imageView: ImageView = itemView.findViewById(R.id.bannerImage)

        fun bind(data: ProjectData) {
            txtName.text = data.projectName
            Glide.with(itemView.context)
                .load(RetrofitClient.Image_Path + data.banner_image)
                .into(imageView)

            itemView.setOnClickListener {
                onItemClick(data) // Invoke the click listener
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_home_banner, parent, false)
        return BannerViewHolder(view)
    }

    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
        holder.bind(banners[position])
    }

    override fun getItemCount(): Int = banners.size
}

