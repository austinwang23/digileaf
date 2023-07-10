package com.example.digileaf

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.digileaf.adapter.JournalAdapter
import com.example.digileaf.database.JournalViewModel
import com.example.digileaf.database.JournalViewModelFactory
import com.example.digileaf.databinding.FragmentJournalListBottomBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class JournalListBottom : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentJournalListBottomBinding
    private lateinit var journalAdapter: JournalAdapter
    private lateinit var journalListEmpty: TextView
    private val journalViewModel: JournalViewModel by viewModels {
        JournalViewModelFactory((requireActivity().application as DigileafApplication).journalRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentJournalListBottomBinding.inflate(inflater, container, false)
        val view = binding.root

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = view.findViewById(R.id.journal_list_RV)
        journalAdapter = JournalAdapter(true)
        recyclerView.adapter = journalAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        journalListEmpty = view.findViewById(R.id.journal_list_empty)
        journalListEmpty.visibility = View.GONE

        val date = arguments?.getString("date")

        if (date != null) {
            journalViewModel.allJournalByDate(date).observe(viewLifecycleOwner, Observer { journalList ->
                if (journalList.isNotEmpty()) {
                    journalAdapter.submitList(journalList)
                    journalListEmpty.visibility = View.GONE
                } else {
                    journalListEmpty.visibility = View.VISIBLE
                }
            })
        }

        journalAdapter.onItemClick = {
            val intent = Intent(requireContext(), JournalActivity::class.java)
            intent.putExtra("journal", it)
            startActivity(intent)
        }
    }

}