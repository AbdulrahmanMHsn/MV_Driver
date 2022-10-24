package com.seamlabs.mvpdriver.authentication

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.seamlabs.mvpdriver.MainActivity
import com.seamlabs.mvpdriver.R
import com.seamlabs.mvpdriver.common.base.BaseFragment
import com.seamlabs.mvpdriver.common.utility.NavigationHelper
import com.seamlabs.mvpdriver.databinding.FragmentCompleteProfileCompanyBinding


class CompleteProfileCompanyFragment : BaseFragment<FragmentCompleteProfileCompanyBinding>() {


    override fun getViewBinding(): FragmentCompleteProfileCompanyBinding {
        return FragmentCompleteProfileCompanyBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).hideBottomNav()

        initView()

        binding.btnSave.setOnClickListener {
            NavigationHelper.navigateInclusive(findNavController(),
                R.id.completeProfileDriverFragment,
                R.id.marketRequestFragment)
        }
    }

    private fun initView() {
        binding.appBarCompleteProfileDriver.setTitle(R.string.complete_your_company_profile)
        binding.appBarCompleteProfileDriver.useSecondActionButton(true, R.drawable.ic_back){
            findNavController().popBackStack()
        }
    }

}