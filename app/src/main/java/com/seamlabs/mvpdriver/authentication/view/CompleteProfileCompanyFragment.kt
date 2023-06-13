package com.seamlabs.mvpdriver.authentication.view

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.seamlabs.mvpdriver.MainActivity
import com.seamlabs.mvpdriver.R
import com.seamlabs.mvpdriver.authentication.viewModel.CompleteProfileViewModel
import com.seamlabs.mvpdriver.authentication.viewModel.SharedViewModel
import com.seamlabs.mvpdriver.common.base.BaseFragment
import com.seamlabs.mvpdriver.common.base.BaseViewModel
import com.seamlabs.mvpdriver.common.utility.*
import com.seamlabs.mvpdriver.databinding.FragmentCompleteProfileCompanyBinding
import com.seamlabs.mvpdriver.models.VehicleType
import com.seamlabs.mvpdriver.models.VehicleTypeModel


class CompleteProfileCompanyFragment : BaseFragment<FragmentCompleteProfileCompanyBinding>() {


    // ViewModel
    private val completeProfileViewModel: CompleteProfileViewModel by activityViewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()


    // Vars
    private var typeOfVehicle: String? = null
    private var imgPath = ""


    private val signalsHandler: (Signal) -> Unit = { signal ->
        when (signal) {
            is Load -> showProgress()
            is StopLoading -> hideProgress()
            is SomethingWentWrong.ErrorMessage -> makeToast(BaseViewModel.errorMessage)
            is SomethingWentWrong.ConnectionFailure -> makeToast(BaseViewModel.errorMessage)
            is Navigate.OnSuccessCompleteCompanyProfile -> {
                NavigationHelper.navigateInclusive(findNavController(),
                    R.id.completeProfileDriverFragment,
                    R.id.marketRequestFragment)
            }
            else -> {}
        }
    }


    override fun getViewBinding(): FragmentCompleteProfileCompanyBinding {
        return FragmentCompleteProfileCompanyBinding.inflate(layoutInflater)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        signalsReceiver.initializeSignalsReceiver(signalsHandler, completeProfileViewModel)

        (activity as MainActivity).hideBottomNav()

        initView()

        btnListener()

        subscribeData()
    }


    private fun initView() {
        binding.appBarCompleteProfileDriver.setTitle(R.string.complete_your_company_profile)
        binding.appBarCompleteProfileDriver.useSecondActionButton(true, R.drawable.ic_back) {
            findNavController().popBackStack()
        }
    }


    private fun subscribeData() {
        currentImagePathLiveData.observe(requireActivity()) { img ->
            img?.let {
                val bitmap = BitmapFactory.decodeFile(it)
                imgPath = it
                try {
                    Glide.with(requireContext())
                        .load(bitmap)
                        .circleCrop()
                        .into(binding.imgCompleteProfile)
                } catch (ex: Exception) {

                }
            }
        }

        sharedViewModel.vehicleType.observe(viewLifecycleOwner){
            typeOfVehicle = it?.first
            binding.edTxtVehicleType.text = it?.second
        }
    }

    private fun btnListener() {

        binding.imgCompleteProfile.setOnClickListener {
            handleProfilePictureChange()
        }

        binding.btnSave.setOnClickListener {
            validation()
        }

        binding.edTxtVehicleType.setOnClickListener {
            openDialogVehicleTypes()
        }

        binding.imgArea.setOnClickListener {
            NavigationHelper.navigate(findNavController(),
                R.id.completeProfileCompanyFragment,
                R.id.mapFragment)
        }
    }


    private fun validation() {
        val companyName = binding.edTxtCompanyName.text.toString()
        val email = binding.edTxtEmail.text.toString().trim()
        val vehicleType = binding.edTxtVehicleType.text.toString()
        val vehicleCount = binding.edTxtNumberOfVehicle.text.toString()

        binding.edTxtCompanyName.error = null
        binding.edTxtVehicleType.error = null
        binding.edTxtNumberOfVehicle.error = null

        when {
            companyName.isEmpty() -> binding.edTxtCompanyName.error =
                getString(R.string.empty_field)
            email.isEmpty() -> binding.edTxtEmail.error = getString(R.string.empty_field)
            email.isNotEmpty() && !binding.edTxtEmail.validateEmail() -> binding.edTxtEmail.error =
                getString(R.string.enter_valid_email)
            vehicleType.isEmpty() -> binding.edTxtVehicleType.error =
                getString(R.string.empty_field)
            typeOfVehicle.isNullOrEmpty() -> binding.edTxtVehicleType.error =
                getString(R.string.empty_field)
            vehicleCount.isEmpty() -> binding.edTxtNumberOfVehicle.error =
                getString(R.string.empty_field)
            vehicleCount.toInt() <= 0 -> binding.edTxtNumberOfVehicle.error =
                getString(R.string.must_be_at_least_one_vehicle)
            completeProfileViewModel.getPreferredArea() == null -> makeToast(getString(R.string.please_select_preferred_working_area))
            else -> {
                completeProfileViewModel.completeCompanyProfile(requireContext(),
                    companyName,
                    email,
                    vehicleCount.toInt(),
                    typeOfVehicle!!,
                    completeProfileViewModel.getPreferredArea()!!.latitude.toString(),
                    completeProfileViewModel.getPreferredArea()!!.longitude.toString(),
                    imgPath)
            }
        }
    }


    private fun openDialogVehicleTypes() {
        val listOfTypes = listOf(
            VehicleTypeModel(1, VehicleType.BUS.name, getString(R.string.buses)),
            VehicleTypeModel(2, VehicleType.CAR.name, getString(R.string.personal_cars)),
        )
        val dialog = SelectVehicleTypeDialog(listOfTypes) {
            typeOfVehicle = it.type
            binding.edTxtVehicleType.text = it.name
            sharedViewModel.setVehicleType(Pair(typeOfVehicle!!,it.name))

        }
        dialog.isCancelable = false
        dialog.show(requireActivity().supportFragmentManager, "VehicleTypes")
    }


}