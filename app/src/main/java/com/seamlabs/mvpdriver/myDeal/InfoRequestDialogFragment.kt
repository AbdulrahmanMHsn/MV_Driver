package com.seamlabs.mvpdriver.myDeal

import android.app.Dialog
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import com.seamlabs.mvpdriver.R
import com.seamlabs.mvpdriver.common.utility.callIntent
import com.seamlabs.mvpdriver.databinding.FragmentInfoRequestDialogBinding
import com.seamlabs.mvpdriver.models.TripModel
import com.seamlabs.mvpdriver.models.TripType
import com.seamlabs.mvpdriver.models.VehicleType


class InfoRequestDialogFragment(private val trip:TripModel) : DialogFragment() {

    private var _binding: FragmentInfoRequestDialogBinding? = null
    private val binding get() = _binding!!

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        if (dialog != null && dialog!!.window != null) {
            dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog!!.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        }
        return super.onCreateDialog(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInfoRequestDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog!!.setCanceledOnTouchOutside(true)

        setData()
        setBtnListeners()
    }

    private fun setData(){
        binding.txtNoOfStudents.text = "${trip.boysCount}" + "${trip.girlsCount}"

        binding.txtDistanceTrips.text = "1.5 KM"

        trip.goTripTime?.let {
            binding.txtDateStartRequest.text = it
        }

        trip.backTripTime?.let {
            binding.txtDateEndRequest.text = it
        }


        binding.txtTitleLocationHome.text = "Almohammadiyah"
        binding.txtDescLocationHome.text = "Mohammadiyah district"

        binding.txtTitleLocation.text = "Almohammadiyah"
        binding.txtDescLocation.text = "Mohammadiyah district"

        when (trip.tripType) {
            TripType.GO_TRIP.name -> {
                binding.txtNoOfTrips.text = "1"
                binding.txtTypeTrip.text = getString(R.string.go_trip)
            }
            TripType.BACK_TRIP.name -> {
                binding.txtNoOfTrips.text = "1"
                binding.txtTypeTrip.text = getString(R.string.back_trip)
            }
            TripType.ROUND_TRIP.name -> {
                binding.txtNoOfTrips.text = "2"
                binding.txtTypeTrip.text = getString(R.string.round_trip)
            }
        }

        when (trip.vehicleType) {
            VehicleType.BUS.name -> {
                binding.txtTypeVehicle.text = getString(R.string.bus)
            }
            VehicleType.CAR.name -> {
                binding.txtTypeVehicle.text = getString(R.string.personal_driver)
            }
            VehicleType.CARPOOLING.name -> {
                binding.txtTypeVehicle.text = getString(R.string.carpooling)
            }
        }
    }


    private fun setBtnListeners() {
        binding.btnCall.setOnClickListener {
            requireActivity().callIntent("")
        }
    }

    override fun onStart() {
        super.onStart()
        setDialogStyle(dialog)
    }


    private fun setDialogStyle(dialog: Dialog?) {
        val window = dialog?.window
        val size = Point()
        val display = window?.windowManager?.defaultDisplay
        display?.getSize(size)
        window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        window?.setGravity(Gravity.BOTTOM)
    }

}