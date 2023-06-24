package com.example.digileaf

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.example.digileaf.entities.Journal
import com.example.digileaf.entities.Plant
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class JournalActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_journal)

        val journal = intent.getParcelableExtra<Journal>("journal")
        val plant = intent.getParcelableExtra<Plant>("plant")
        if(journal != null){
            val journalDate : TextView = findViewById(R.id.journal_page_date)
            val journalPlantName : TextView = findViewById(R.id.journal_page_plant_name)
            val journalImage : ImageView = findViewById(R.id.journal_page_image)
            val journalEntry: TextView = findViewById(R.id.journal_page_entry)

            journalEntry.text = journal.entry
            journalPlantName.text = plant?.name ?: ""

            if (journal.imagePath != "") {
                val imageFile = getFileStreamPath(journal.imagePath)
                // If for whatever reason, the file no longer exists
                if(imageFile == null || !imageFile.exists()) {
                    journalImage.setImageResource(R.drawable.default_plant)
                } else {
                    val bitmap = BitmapFactory.decodeFile(imageFile.absolutePath)
                    journalImage.setImageBitmap(bitmap)
                }
            }
            val timestamp = journal.timestamp
            val dateFormat = SimpleDateFormat("MMMM d yyyy", Locale.getDefault())
            val formattedDate = dateFormat.format(Date(timestamp))
            journalDate.text = formattedDate
        }

        val backButton : ImageButton = findViewById(R.id.journal_page_back_button)
        backButton.setOnClickListener{
            finish()
        }
    }

    override fun onBackPressed() {
        finish()
    }
}