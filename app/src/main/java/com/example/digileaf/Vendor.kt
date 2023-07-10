package com.example.digileaf

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.digileaf.databinding.FragmentVendorDialogBinding
import org.osmdroid.views.overlay.Marker


interface VendorDialogListener {
    fun navigateToVendor(marker: Marker)
}

class VendorDetail(marker: Marker, listener: VendorDialogListener): DialogFragment() {
    private lateinit var binding: FragmentVendorDialogBinding
    private var locationMarker: Marker
    private var callbackListener: VendorDialogListener

    init {
        this.locationMarker = marker
        this.callbackListener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentVendorDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val vendorDetails = locationMarker.snippet.split(",", limit = 2)

        binding.vendorName.text = vendorDetails[0]
        binding.vendorAddress.text = vendorDetails[1]

        binding.navigationButton.setOnClickListener{
            callbackListener.navigateToVendor(locationMarker)
            dismiss()
        }
    }
}