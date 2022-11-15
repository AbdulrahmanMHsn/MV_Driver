package com.seamlabs.mvpdriver.authentication.view

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.seamlabs.mvpdriver.MainActivity
import com.seamlabs.mvpdriver.R
import com.seamlabs.mvpdriver.authentication.viewModel.CompleteProfileViewModel
import com.seamlabs.mvpdriver.common.base.BaseFragment
import com.seamlabs.mvpdriver.common.base.BaseViewModel
import com.seamlabs.mvpdriver.common.utility.*
import com.seamlabs.mvpdriver.databinding.FragmentCompleteProfileDriverBinding
import com.seamlabs.mvpdriver.models.VehicleModel
import com.seamlabs.mvpdriver.models.VehicleType
import com.seamlabs.mvpdriver.models.VehicleTypeModel


class CompleteProfileDriverFragment : BaseFragment<FragmentCompleteProfileDriverBinding>() {

    // ViewModels
    private val completeProfileViewModel: CompleteProfileViewModel by activityViewModels()

    // Vars
    private var selectedGender: String? = ""

    private val signalsHandler: (Signal) -> Unit = { signal ->
        when (signal) {
            is Load -> showProgress()
            is StopLoading -> hideProgress()
            is SomethingWentWrong.ErrorMessage -> makeToast(BaseViewModel.errorMessage)
            is SomethingWentWrong.ConnectionFailure -> makeToast(BaseViewModel.errorMessage)
            is Navigate.OnSuccessCompleteIndividualProfile -> {
                NavigationHelper.navigateInclusive(findNavController(),
                    R.id.completeProfileDriverFragment,
                    R.id.marketRequestFragment)
            }
            else -> {}
        }
    }

    override fun getViewBinding(): FragmentCompleteProfileDriverBinding {
        return FragmentCompleteProfileDriverBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        signalsReceiver.initializeSignalsReceiver(signalsHandler, completeProfileViewModel)
        (activity as MainActivity).hideBottomNav()

        initView()
        addVehicle()
        btnListener()

    }

    private fun initView() {
        binding.appBarCompleteProfileDriver.setTitle(R.string.complete_your_driver_profile)
        binding.appBarCompleteProfileDriver.useSecondActionButton(true, R.drawable.ic_back) {
            findNavController().popBackStack()
        }
    }


    private fun btnListener() {
        binding.btnAddVehicle.setOnClickListener {
            addVehicle(true)
        }

        binding.edTxtGender.setOnClickListener {
            openDialogGenderTypes()
        }

        binding.btnSave.setOnClickListener {
            validationData()
        }

        binding.imgArea.setOnClickListener {
            NavigationHelper.navigate(
                findNavController(),
                R.id.completeProfileDriverFragment,
                R.id.mapFragment,
            )
        }
    }

    private fun validationData() {
        val fullName = binding.edTxtFullName.text.toString()
        val email = binding.edTxtEmail.text.toString()
        val gender = binding.edTxtGender.text.toString()
        val count = binding.layoutAddVehicle.childCount



        when {
            fullName.isEmpty() -> {
                binding.edTxtFullName.error = getString(R.string.empty_field)
                binding.edTxtFullName.requestFocus()
                binding.scrollDriver.scrollTo(0,0)
            }
            email.isEmpty() -> {
                binding.edTxtEmail.error = getString(R.string.empty_field)
                binding.edTxtEmail.requestFocus()
                binding.scrollDriver.scrollTo(0,0)
            }
            email.isNotEmpty() && binding.edTxtEmail.validateEmail() -> {
                binding.edTxtEmail.error = getString(R.string.enter_valid_email)
                binding.edTxtEmail.requestFocus()
                binding.scrollDriver.scrollTo(0,0)
            }
            gender.isEmpty() -> {
                binding.edTxtGender.error = getString(R.string.empty_field)
                binding.edTxtGender.requestFocus()
                binding.scrollDriver.scrollTo(0,0)
            }
            completeProfileViewModel.getPreferredArea() == null -> {
                makeToast(getString(R.string.please_select_preferred_working_area))
            }

            else -> {
                completeProfileViewModel.enqueueSignal(Load)
                val listOfVehicles = mutableListOf<VehicleModel>()

                var type = ""

                for (i in 0 until count) {
                    val vehicleType = binding.layoutAddVehicle.getChildAt(i)
                        .findViewById<TextView>(R.id.edTxt_vehicle_type).text.toString()
                    val brand = binding.layoutAddVehicle.getChildAt(i)
                        .findViewById<EditText>(R.id.edTxt_car_brand).text.toString()
                    val model = binding.layoutAddVehicle.getChildAt(i)
                        .findViewById<EditText>(R.id.edTxt_car_model).text.toString()
                    val manufacturingYear = binding.layoutAddVehicle.getChildAt(i)
                        .findViewById<EditText>(R.id.edTxt_car_manufacturing_year).text.toString()


                    when (vehicleType) {
                        getString(R.string.buses) -> type = VehicleType.BUS.name
                        getString(R.string.personal_cars) -> type = VehicleType.CAR.name
                        getString(R.string.carpooling) -> type = VehicleType.CARPOOLING.name
                    }

                    listOfVehicles.add(VehicleModel(i, type, model, brand, manufacturingYear))
                }
                completeProfileViewModel.completeIndividualProfile(
                    requireContext(),
                    fullName,
                    email,
                    selectedGender ?: "",
                    listOfVehicles,
                    completeProfileViewModel.getPreferredArea()!!.latitude.toString(),
                    completeProfileViewModel.getPreferredArea()!!.longitude.toString(),
                )
            }
        }

    }


    private fun addVehicle(isAdded: Boolean = false) {
        val child = LayoutInflater.from(requireContext())
            .inflate(R.layout.layout_section_add_vehicle, null)
        val vehicleType = child.findViewById<TextView>(R.id.edTxt_vehicle_type)
        val brand = child.findViewById<EditText>(R.id.edTxt_car_brand)
        val model = child.findViewById<EditText>(R.id.edTxt_car_model)
        val manufacturingYear = child.findViewById<EditText>(R.id.edTxt_car_manufacturing_year)
        val line = child.findViewById<View>(R.id.view_line)

        vehicleType.setOnClickListener {
            openDialogVehicleTypes { name, type ->
                vehicleType.text = name
            }
        }

        if (isAdded) {
            line.visibility = View.VISIBLE
        }

        binding.layoutAddVehicle.addView(child)
    }

    private fun openDialogVehicleTypes(value: (String, String) -> Unit) {
        val listOfTypes = listOf(
            VehicleTypeModel(1, VehicleType.BUS.name, getString(R.string.buses)),
            VehicleTypeModel(2, VehicleType.CAR.name, getString(R.string.personal_cars)),
            VehicleTypeModel(3, VehicleType.CARPOOLING.name, getString(R.string.carpooling)),
        )
        val dialog = SelectVehicleTypeDialog(listOfTypes) {
            value(it.name, it.type)
        }
        dialog.isCancelable = false
        dialog.show(requireActivity().supportFragmentManager, "VehicleTypes")
    }

    private fun openDialogGenderTypes() {
        val dialog = GenderTypeDialog { name, value ->
            binding.edTxtGender.text = name
            selectedGender = value
        }
        dialog.isCancelable = false
        dialog.show(requireActivity().supportFragmentManager, "GenderTypes")
    }

}