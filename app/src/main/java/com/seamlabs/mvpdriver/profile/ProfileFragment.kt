package com.seamlabs.mvpdriver.profile


import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.seamlabs.mvpdriver.MainActivity
import com.seamlabs.mvpdriver.R
import com.seamlabs.mvpdriver.common.base.BaseFragment
import com.seamlabs.mvpdriver.databinding.FragmentProfileBinding


class ProfileFragment : BaseFragment<FragmentProfileBinding>() {


    override fun getViewBinding(): FragmentProfileBinding {
        return FragmentProfileBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        (activity as MainActivity).showBottomNav()
        binding.appBarProfile.setTitle(R.string.profile)
        binding.appBarProfile.useSecondActionButton(true, R.drawable.ic_notification) {
            findNavController().navigate(R.id.notificationsFragment)
        }
    }

}