package com.example.digileaf

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.digileaf.entities.Plant
import com.example.digileaf.PlantAdapter.PlantViewHolder

class PlantAdapter: ListAdapter<Plant, PlantViewHolder>(PLANT_COMPARATOR) {

    var onItemClick : ((Plant) -> Unit)? = null

    inner class PlantViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val plantImageView: ImageView = itemView.findViewById(R.id.plant_item_image)
        val plantNameView: TextView = itemView.findViewById(R.id.plant_item_name)
        val plantSpeciesView: TextView = itemView.findViewById(R.id.plant_item_species)
        val plantAgeView: TextView = itemView.findViewById(R.id.plant_item_age)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlantViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.plant_item, parent, false)

        return PlantViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlantViewHolder, position: Int) {
        val plant = getItem(position)
        // TODO - Store image path and get image resource
        holder.plantImageView.setImageResource(R.drawable.image_1)
        holder.plantNameView.text = plant.name
        holder.plantSpeciesView.text = plant.species

        // TODO - Get rid of plant age
        holder.plantAgeView.text = "2 mos"

        holder.itemView.setOnClickListener{
            onItemClick?.invoke(plant)
        }
    }

    companion object {
        private val PLANT_COMPARATOR = object : DiffUtil.ItemCallback<Plant>() {
            override fun areItemsTheSame(oldItem: Plant, newItem: Plant): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: Plant, newItem: Plant): Boolean {
                // TODO - Find a better way to do this
                return ((oldItem.name == newItem.name) &&
                        (oldItem.description == newItem.description) &&
                        (oldItem.species == newItem.species) &&
                        (oldItem.imagePath == newItem.imagePath))
            }
        }
    }

}