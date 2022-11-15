package com.seamlabs.mvpdriver.profile

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.seamlabs.mvpdriver.MainActivity
import com.seamlabs.mvpdriver.R
import com.seamlabs.mvpdriver.common.base.BaseFragment
import com.seamlabs.mvpdriver.common.utility.NavigationHelper
import com.seamlabs.mvpdriver.databinding.FragmentAppSettingBinding


class AppSettingFragment : BaseFragment<FragmentAppSettingBinding>() {


    override fun getViewBinding(): FragmentAppSettingBinding {
        return FragmentAppSettingBinding.inflate(layoutInflater)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        btnListener()
    }

    private fun initView() {
        (activity as MainActivity).hideBottomNav()
        binding.appBarAppSettings.setTitle(R.string.profile)
        binding.appBarAppSettings.useBackButton(true) {
            findNavController().popBackStack()
        }
    }


    private fun btnListener() {
        binding.changeLanguageLayout.setOnClickListener {
            NavigationHelper.navigate(findNavController(),
                R.id.appSettingFragment,
                R.id.changeLanguageFragment)
        }
    }

}