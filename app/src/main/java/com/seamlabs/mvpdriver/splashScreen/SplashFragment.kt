package com.seamlabs.mvpdriver.splashScreen

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.seamlabs.mvpdriver.MainActivity
import com.seamlabs.mvpdriver.R
import com.seamlabs.mvpdriver.common.base.BaseFragment
import com.seamlabs.mvpdriver.common.utility.Navigate
import com.seamlabs.mvpdriver.common.utility.NavigationHelper
import com.seamlabs.mvpdriver.common.utility.StopLoading
import com.seamlabs.mvpdriver.common.utility.UserPreferences
import com.seamlabs.mvpdriver.databinding.FragmentSplashScreenBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SplashFragment : BaseFragment<FragmentSplashScreenBinding>() {


    override fun getViewBinding(): FragmentSplashScreenBinding {
        return FragmentSplashScreenBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity).hideBottomNav()

        Glide.with(requireContext())
            .asGif()
            .load(R.drawable.splash_gif)
            .into(binding.imgSplash)


        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                delay(3000)
                if (UserPreferences.getFirstTimeOpenAppState(requireContext())) {
                    if (UserPreferences.getLoginState(requireContext())) {
                        NavigationHelper.navigateInclusive(findNavController(),
                            R.id.splashScreenFragment,
                            R.id.marketRequestFragment)
                    } else {
                        NavigationHelper.navigateInclusive(findNavController(),
                            R.id.splashScreenFragment,
                            R.id.loginFragment)
                    }
                } else {
                    NavigationHelper.navigateInclusive(findNavController(),
                        R.id.splashScreenFragment,
                        R.id.signUpFragment)
                }
            }
        }
    }

}