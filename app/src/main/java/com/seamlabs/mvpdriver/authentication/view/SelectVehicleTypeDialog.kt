package com.seamlabs.mvpdriver.authentication.view

import android.app.Dialog
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.seamlabs.mvpdriver.authentication.adapter.VehicleTypesAdapter
import com.seamlabs.mvpdriver.databinding.DialogSelectVehicleTypeBinding
import com.seamlabs.mvpdriver.models.VehicleTypeModel

class SelectVehicleTypeDialog(private val listOfTypes:List<VehicleTypeModel>,private val callbackSelectedType:(VehicleTypeModel)->Unit):DialogFragment() {

    // Binding
    private var _binding: DialogSelectVehicleTypeBinding? = null
    private val binding get() = _binding!!

    // Adapter
    private lateinit var vehicleTypesAdapter:VehicleTypesAdapter

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
        _binding = DialogSelectVehicleTypeBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog!!.setCanceledOnTouchOutside(true)

        setupRecyclerViewVehicleTypes()
    }


    private fun setupRecyclerViewVehicleTypes() {
        binding.rcVwVehicleTypes.apply {
            vehicleTypesAdapter = VehicleTypesAdapter {
                callbackSelectedType(it)
                dialog?.dismiss()
            }
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = vehicleTypesAdapter
        }
        vehicleTypesAdapter.submitList(listOfTypes)
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