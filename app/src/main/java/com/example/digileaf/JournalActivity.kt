package com.example.digileaf

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.example.digileaf.entities.Journal

class JournalActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_journal)

        val journal = intent.getParcelableExtra<Journal>("journal")
        if(journal != null){
            val journalDate : TextView = findViewById(R.id.journal_page_date)
            val journalPlantName : TextView = findViewById(R.id.journal_page_plant_name)
            val journalImage : ImageView = findViewById(R.id.journal_page_image)
            val journalEntry: TextView = findViewById(R.id.journal_page_entry)

            journalEntry.text = journal.entry
            journalPlantName.text = journal.plantName ?: ""

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
            journalDate.text = journal.date
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