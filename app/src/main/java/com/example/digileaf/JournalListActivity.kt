package com.example.digileaf

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.digileaf.adapter.JournalAdapter
import com.example.digileaf.database.JournalViewModel
import com.example.digileaf.database.JournalViewModelFactory
import com.example.digileaf.entities.Plant

class JournalListActivity : AppCompatActivity() {
    private lateinit var backButton: ImageButton
    private lateinit var journalAdapter: JournalAdapter
    private val journalViewModel: JournalViewModel by viewModels {
        JournalViewModelFactory((this.application as DigileafApplication).journalRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_journal_list)

        val recyclerView: RecyclerView = findViewById(R.id.journal_list_RV)
        journalAdapter = JournalAdapter()
        recyclerView.adapter = journalAdapter
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        val date = intent.getStringExtra("date")

        if (date != null) {
            journalViewModel.allJournalByDate(date).observe(this, Observer {
                it.let { journalAdapter.submitList(it) }
            })
        }

        journalAdapter.onItemClick = {
            val intent = Intent(this, JournalActivity::class.java)
            intent.putExtra("journal", it)
            startActivity(intent)
        }

        // Set up back button listener
        backButton = findViewById(R.id.journal_list_back_button)
        backButton.setOnClickListener {
            val addJournalIntent = Intent()
            setResult(Activity.RESULT_CANCELED, addJournalIntent)
            finish()
        }
    }
}