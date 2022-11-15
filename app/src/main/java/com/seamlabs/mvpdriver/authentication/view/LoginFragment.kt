package com.seamlabs.mvpdriver.authentication

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.seamlabs.mvpdriver.MainActivity
import com.seamlabs.mvpdriver.R
import com.seamlabs.mvpdriver.authentication.viewModel.AuthViewModel
import com.seamlabs.mvpdriver.common.base.BaseFragment
import com.seamlabs.mvpdriver.common.base.BaseViewModel
import com.seamlabs.mvpdriver.common.utility.*
import com.seamlabs.mvpdriver.databinding.FragmentLoginBinding


class LoginFragment : BaseFragment<FragmentLoginBinding>() {


    private val authViewModel: AuthViewModel by viewModels()


    private val signalsHandler: (Signal) -> Unit = { signal ->
        when (signal) {
            is Load -> showProgress()
            is StopLoading -> hideProgress()
            is SomethingWentWrong.ErrorMessage -> makeToast(BaseViewModel.errorMessage)
            is SomethingWentWrong.ConnectionFailure -> makeToast(BaseViewModel.errorMessage)
            is SomethingWentWrong.BadCredentials -> {
                binding.wrongCredentialsLogin.visibility = View.VISIBLE
            }
            is Navigate.SuccessLoginNavigate -> {
                NavigationHelper.navigateInclusive(findNavController(),
                    R.id.loginFragment, R.id.marketRequestFragment)
            }
            is Navigate.ProfileUncompleted -> {
                NavigationHelper.navigate(findNavController(),
                    R.id.loginFragment, R.id.chooseUserTypeFragment)
            }
            else -> {}
        }
    }


    override fun getViewBinding(): FragmentLoginBinding {
        return FragmentLoginBinding.inflate(layoutInflater)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        signalsReceiver.initializeSignalsReceiver(signalsHandler, authViewModel)

        (activity as MainActivity).hideBottomNav()

        btnListener()

    }


    private fun btnListener() {
        binding.loginBtnLogin.setOnClickListener {
            binding.wrongCredentialsLogin.visibility = View.GONE
            login()
        }

        binding.signupLogin.setOnClickListener {
            NavigationHelper.navigate(findNavController(),
                R.id.loginFragment, R.id.signUpFragment)
        }
    }


    private fun login() {
        val countryCode = binding.countryCodeLogin.selectedCountryCode
        val phoneNumber = binding.phoneNumberLogin.text.toString()
        val password = binding.passwordLogin.text.toString()

        binding.phoneNumberLogin.error = null
        binding.passwordLogin.error = null

        when {
            phoneNumber.isEmpty() -> {
                binding.phoneNumberLogin.error = getString(R.string.empty_field)
            }
            password.isEmpty() -> {
                binding.passwordLogin.error = getString(R.string.empty_field)
            }
            else -> {
                authViewModel.login(requireContext(), countryCode, phoneNumber, password)
            }
        }
    }


}