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
import com.seamlabs.mvpdriver.databinding.FragmentSignUpBinding
import java.util.regex.Matcher
import java.util.regex.Pattern


class SignUpFragment : BaseFragment<FragmentSignUpBinding>() {

    private val authViewModel:AuthViewModel by viewModels()

    private val signalsHandler: (Signal) -> Unit = { signal ->
        when (signal) {
            is Load -> showProgress()
            is StopLoading -> hideProgress()
            is SomethingWentWrong.ErrorMessage -> makeToast(BaseViewModel.errorMessage)
            is SomethingWentWrong.ConnectionFailure -> makeToast(BaseViewModel.errorMessage)
            is Navigate.SuccessRegisterNavigate -> {
                NavigationHelper.navigate(findNavController(),R.id.signUpFragment,R.id.chooseUserTypeFragment)
            }
            else->{}

        }
    }


    override fun getViewBinding(): FragmentSignUpBinding {
        return FragmentSignUpBinding.inflate(layoutInflater)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        signalsReceiver.initializeSignalsReceiver(signalsHandler, authViewModel)
        (activity as MainActivity).hideBottomNav()

        btnListener()

        UserPreferences.setFirstTimeOpenAppState(requireContext(),true)

    }

    private fun btnListener(){

        binding.btnSignup.setOnClickListener {
            register()
        }

        binding.txtSignIn.setOnClickListener {
            NavigationHelper.navigateInclusive(findNavController(),R.id.signUpFragment,R.id.loginFragment)
        }

    }

    private fun register(){
        val countryCode = binding.countryCodeSignUp.selectedCountryCode
        val phoneNumber = binding.phoneNumberSignUp.text.toString()
        val password = binding.passwordSignUp.text.toString()

        binding.phoneNumberSignUp.error = null
        binding.passwordSignUp.error = null

        when {
            phoneNumber.isEmpty() -> {
                binding.phoneNumberSignUp.error = getString(R.string.empty_field)
            }
            password.isEmpty() -> {
                binding.passwordSignUp.error = getString(R.string.empty_field)
            }
            password.isNotEmpty() && !isValidPassword(password) -> {
                binding.passwordSignUp.error = getString(R.string.password_must_contain)
            }
            else -> {
                authViewModel.register(requireContext(), countryCode, phoneNumber, password)
            }
        }
    }

    fun isValidPassword(password: String): Boolean {
        val pattern: Pattern =
            Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9]).{8,}\$")
        val matcher: Matcher = pattern.matcher(password)
        return matcher.matches()
    }
}