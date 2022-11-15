package com.seamlabs.mvpdriver.profile


import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.seamlabs.mvpdriver.MainActivity
import com.seamlabs.mvpdriver.R
import com.seamlabs.mvpdriver.common.base.BaseFragment
import com.seamlabs.mvpdriver.common.base.BaseViewModel
import com.seamlabs.mvpdriver.common.utility.*
import com.seamlabs.mvpdriver.databinding.FragmentProfileBinding
import com.seamlabs.mvpdriver.models.Driver


class ProfileFragment : BaseFragment<FragmentProfileBinding>() {


    // ViewModel
    private val profileViewModel: ProfileViewModel by viewModels()


    private val signalsHandler: (Signal) -> Unit = { signal ->
        when (signal) {
            is Load -> showProgress()
            is StopLoading -> hideProgress()
            is SomethingWentWrong.ErrorMessage -> makeToast(BaseViewModel.errorMessage)
            is SomethingWentWrong.ConnectionFailure -> makeToast(BaseViewModel.errorMessage)
            is Navigate.OnSuccessLogout -> {
                NavigationHelper.navigateInclusive(findNavController(),
                    R.id.profileFragment,
                    R.id.loginFragment)
            }
            else -> {}
        }
    }


    override fun getViewBinding(): FragmentProfileBinding {
        return FragmentProfileBinding.inflate(layoutInflater)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        signalsReceiver.initializeSignalsReceiver(signalsHandler, profileViewModel)
        profileViewModel.getProfile(requireContext())
        initView()
        btnListener()
        subscribeData()
    }


    private fun initView() {
        (activity as MainActivity).showBottomNav()
        binding.appBarProfile.setTitle(R.string.profile)
//        binding.appBarProfile.useSecondActionButton(true, R.drawable.ic_notification) {
//            findNavController().navigate(R.id.notificationsFragment)
//        }
    }


    private fun btnListener() {
        binding.logOutLayout.setOnClickListener {
            profileViewModel.logout(requireContext())
        }

        binding.appSettingLayout.setOnClickListener {
            NavigationHelper.navigate(findNavController(),
                R.id.profileFragment,
                R.id.appSettingFragment)
        }
    }


    private fun subscribeData() {
        profileViewModel.profileLData.observe(viewLifecycleOwner) { user ->
            user?.let {
                setData(it)
            }
        }
    }


    private fun setData(user: Driver) {
        user.profileableType?.let {
            if (user.profileableType == "company_profile") {
                binding.userNameNewProfile.text = user.profileable.companyName
                binding.txtVehicleCount.text = user.profileable.vehicleCount
                binding.userRoleNewProfile.text = getString(R.string.company)
            } else {
                binding.userNameNewProfile.text = user.profileable.fullName
                binding.txtVehicleCount.text = user.vehicles.size.toString()
                binding.userRoleNewProfile.text = getString(R.string.driver)
            }
        }

        user.profileableId?.let {
            binding.userPhoneIdNewProfile.text = "${user.phoneNumber} | $it"
        }

        Glide.with(requireContext())
            .load(user.image)
            .circleCrop()
            .error(R.drawable.ic_placeholder_profile)
            .into(binding.profileImage)
    }

}