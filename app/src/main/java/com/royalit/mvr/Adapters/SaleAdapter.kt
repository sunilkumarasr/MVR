package com.royalit.mvr.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.royalit.mvr.Model.SaleData
import com.royalit.mvr.R

class SaleAdapter(
    private val items: List<SaleData>,
    private val onItemClick: (SaleData) -> Unit // Click listener function
) : RecyclerView.Adapter<SaleAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtName: TextView = itemView.findViewById(R.id.txtName)
        val txtCPrice: TextView = itemView.findViewById(R.id.txtCPrice)
        val txtPrice: TextView = itemView.findViewById(R.id.txtPrice)
        val txtDate: TextView = itemView.findViewById(R.id.txtDate)
        val txtPlot: TextView = itemView.findViewById(R.id.txtPlot)
        val txtfacing: TextView = itemView.findViewById(R.id.txtfacing)
        val txtDes: TextView = itemView.findViewById(R.id.txtDes)
        val txtStatus: TextView = itemView.findViewById(R.id.txtStatus)

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
            .inflate(R.layout.item_sale, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.txtName.text = item.project_name
        holder.txtPrice.text = "₹"+item.sale_amount
        holder.txtCPrice.text = "+₹"+item.employee_commision
        holder.txtDate.text = "Date: "+item.date
        holder.txtPlot.text = "Plot No: "+item.plot_no
        holder.txtDes.text = item.description

        if (!item.facing.equals("")){
            holder.txtfacing.visibility = View.VISIBLE
            holder.txtfacing.text = "Facing: "+item.facing
        }

        holder.txtStatus.text = item.Sale_status
        holder.txtStatus.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.credit))
        holder.txtStatus.setBackgroundResource(R.drawable.round_credited)

//        if (item.status.equals("0")){
//            holder.txtStatus.text = " Requested "
//            holder.txtStatus.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.requested))
//            holder.txtStatus.setBackgroundResource(R.drawable.round_requested)
//        }
//        if (item.status.equals("1")){
//            holder.txtStatus.text = " Approved "
//            holder.txtStatus.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.approve))
//            holder.txtStatus.setBackgroundResource(R.drawable.round_approved)
//        }
//        if (item.status.equals("2")){
//            holder.txtStatus.text = " Rejected "
//            holder.txtStatus.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.reject))
//            holder.txtStatus.setBackgroundResource(R.drawable.round_reject)
//        }
//        if (item.status.equals("3")){
//            holder.txtStatus.text = " Credited"
//            holder.txtStatus.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.credit))
//            holder.txtStatus.setBackgroundResource(R.drawable.round_credited)
//        }

    }

    override fun getItemCount(): Int {
        return items.size
    }

}