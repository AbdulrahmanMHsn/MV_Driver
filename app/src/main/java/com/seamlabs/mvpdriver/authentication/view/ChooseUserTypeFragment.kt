package com.seamlabs.mvpdriver.authentication

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.seamlabs.mvpdriver.MainActivity
import com.seamlabs.mvpdriver.R
import com.seamlabs.mvpdriver.common.base.BaseFragment
import com.seamlabs.mvpdriver.common.utility.NavigationHelper
import com.seamlabs.mvpdriver.databinding.FragmentChooseUserTypeBinding

class ChooseUserTypeFragment : BaseFragment<FragmentChooseUserTypeBinding>() {

    override fun getViewBinding(): FragmentChooseUserTypeBinding {
        return FragmentChooseUserTypeBinding.inflate(layoutInflater)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        btnListener()
    }


    private fun initView() {
        (activity as MainActivity).hideBottomNav()
        binding.appBarChooseAccountType.setTitle(R.string.choose_account_type)
        binding.appBarChooseAccountType.useSecondActionButton(true, R.drawable.ic_back){
            findNavController().popBackStack()
        }
    }

    private fun btnListener() {

        binding.cardCompany.layoutCardPickup.setOnClickListener {
            NavigationHelper.navigate(findNavController(),R.id.chooseUserTypeFragment,R.id.completeProfileCompanyFragment)
        }

        binding.cardDriver.layoutCardTransportation.setOnClickListener {
            NavigationHelper.navigate(findNavController(),R.id.chooseUserTypeFragment,R.id.completeProfileDriverFragment)
        }
    }


}