package com.example.digileaf

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.digileaf.model.Plant

class PlantAdapter(
    private val plantList: ArrayList<Plant>,
    private val listener: OnItemClickListener
) :
    RecyclerView.Adapter<PlantAdapter.PlantViewHolder>() {

    inner class PlantViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val imageView: ImageView = itemView.findViewById(R.id.plant_item_image)
        val textView: TextView = itemView.findViewById(R.id.plant_item_name)

        init {
            itemView.setOnClickListener(this);
        }

        override fun onClick(v: View?) {
            val position = absoluteAdapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlantViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.plant_item, parent, false)

        return PlantViewHolder(view)
    }

    override fun getItemCount(): Int {
        return plantList.size
    }

    override fun onBindViewHolder(holder: PlantViewHolder, position: Int) {
        val plant = plantList[position]
        holder.imageView.setImageResource(plant.plantImage)
        holder.textView.text = plant.plantName
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int);
    }
}