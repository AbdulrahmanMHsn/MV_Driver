package com.seamlabs.mvpdriver.marketRequests

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewManager
import androidx.navigation.fragment.findNavController
import com.seamlabs.mvpdriver.MainActivity
import com.seamlabs.mvpdriver.R
import com.seamlabs.mvpdriver.common.base.BaseFragment
import com.seamlabs.mvpdriver.common.utility.NavigationHelper
import com.seamlabs.mvpdriver.databinding.FragmentSuccessfullySubmittedOfferBinding


class SuccessfullySubmittedOfferFragment : BaseFragment<FragmentSuccessfullySubmittedOfferBinding>() {


    override fun getViewBinding(): FragmentSuccessfullySubmittedOfferBinding {
        return FragmentSuccessfullySubmittedOfferBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity).hideBottomNav()

        binding.btnSubmitRequest.setOnClickListener {
            NavigationHelper.navigateInclusive(findNavController(),R.id.successfullySubmittedOfferFragment,R.id.marketRequestFragment)
        }
    }

}