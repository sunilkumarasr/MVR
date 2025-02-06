package com.royalit.mvr.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.royalit.mvr.Model.TransactionsData
import com.royalit.mvr.R

class TransactionsAdapter(
    private val items: List<TransactionsData>,
    private val onItemClick: (TransactionsData) -> Unit // Click listener function
) : RecyclerView.Adapter<TransactionsAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtPlot: TextView = itemView.findViewById(R.id.txtPlot)
        val txtName: TextView = itemView.findViewById(R.id.txtName)
        val txtNote: TextView = itemView.findViewById(R.id.txtNote)
        val txtPrice: TextView = itemView.findViewById(R.id.txtPrice)
        val txtDate: TextView = itemView.findViewById(R.id.txtDate)
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
            .inflate(R.layout.item_transaction, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.txtPlot.text = "Plot No: "+item.plotNo
        holder.txtName.text = "Withdrawal"
        holder.txtNote.text = item.notes
        holder.txtDate.text = item.createdAt
        holder.txtPrice.text = "₹ "+item.amount

        if (item.status.equals("0")){
            holder.txtStatus.text = "Requested"
            holder.txtStatus.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.requested))
            holder.txtStatus.setBackgroundResource(R.drawable.round_requested)
        }
        if (item.status.equals("1")){
            holder.txtStatus.text = "Approved"
            holder.txtStatus.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.approve))
            holder.txtStatus.setBackgroundResource(R.drawable.round_approved)
        }
        if (item.status.equals("2")){
            holder.txtStatus.text = "Rejected"
            holder.txtStatus.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.reject))
            holder.txtStatus.setBackgroundResource(R.drawable.round_reject)
        }
        if (item.status.equals("3")){
            holder.txtStatus.text = "Credited"
            holder.txtStatus.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.credit))
            holder.txtStatus.setBackgroundResource(R.drawable.round_credited)
        }


    }

    override fun getItemCount(): Int {
        return items.size
    }
}