package com.example.digileaf.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.digileaf.R
import com.example.digileaf.entities.Achievement

class AchievementAdapter(private val achievements: List<Achievement>) :
    RecyclerView.Adapter<AchievementAdapter.AchievementViewHolder>() {

    class AchievementViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Define views within the item layout
        val cardView: CardView = itemView.findViewById(R.id.achievement_card_view)
        val badgeImage: ImageView = itemView.findViewById(R.id.achievement_badge_image)
        val titleText: TextView = itemView.findViewById(R.id.achievement_title_text)
        val descriptionText: TextView = itemView.findViewById(R.id.achievement_description_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AchievementViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_achievement, parent, false)
        return AchievementViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AchievementViewHolder, position: Int) {
        val achievement = achievements[position]
        holder.badgeImage.setImageResource(achievement.image)
        holder.titleText.text = achievement.title
        holder.descriptionText.text = achievement.description

        // Check if the achievement is locked
        if (achievement.unlocked) {
            // Remove foreground overlay from unlocked achievements
            holder.cardView.foreground = null
        } else {
            // Apply foreground overlay to locked achievements
            holder.cardView.foreground = ContextCompat.getDrawable(holder.itemView.context, R.drawable.fg_overlay)
        }
    }

    override fun getItemCount(): Int {
        return achievements.size
    }
}


