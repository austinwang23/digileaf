
package com.example.digileaf

import android.annotation.SuppressLint
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.appcompat.content.res.AppCompatResources.getColorStateList
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.digileaf.database.PlantViewModelFactory
import com.example.digileaf.database.PlantViewModel
import com.example.digileaf.databinding.FragmentUpdatePlantStatusBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.time.LocalTime

class UpdatePlantStatus(listener: UpdatePlantStatusDialogListener) : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentUpdatePlantStatusBinding
    private var watered = false
    private var fertilized = false
    private var groomed = false
    private var callbackListener: UpdatePlantStatusDialogListener?=null

    init {
        this.callbackListener = listener
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbackListener = context as UpdatePlantStatusDialogListener?
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.updatePlantStatusButton.setOnClickListener{
            updatePlantStatus()
        }

        binding.fertilizerIcon.setOnClickListener{
            toggleFertilize()
        }

        binding.waterIcon.setOnClickListener{
            toggleWater()
        }

        binding.groomingIcon.setOnClickListener{
            toggleGroom()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUpdatePlantStatusBinding.inflate(inflater, container, false)
        val view = binding.root

        return view
    }

    private fun updatePlantStatus() {
        callbackListener?.updatePlantStatus(watered, fertilized, groomed)
        dismiss()
    }

    private fun toggleWater() {
        watered = !watered
        when(watered) {
            true -> binding.waterIcon.setBackgroundResource(R.drawable.bg_circle)
            false -> binding.waterIcon.setBackgroundResource(R.drawable.bg_circle_gray)
        }
    }

    private fun toggleFertilize() {
        fertilized = !fertilized
        when(fertilized) {
            true -> binding.fertilizerIcon.setBackgroundResource(R.drawable.bg_circle)
            false -> binding.fertilizerIcon.setBackgroundResource(R.drawable.bg_circle_gray)
        }
    }

    private fun toggleGroom() {
        groomed = !groomed
        when(groomed) {
            true -> binding.groomingIcon.setBackgroundResource(R.drawable.bg_circle)
            false -> binding.groomingIcon.setBackgroundResource(R.drawable.bg_circle_gray)
        }
    }

    interface UpdatePlantStatusDialogListener {
        fun updatePlantStatus(watered: Boolean, fertilized: Boolean, groomed: Boolean)
    }
}