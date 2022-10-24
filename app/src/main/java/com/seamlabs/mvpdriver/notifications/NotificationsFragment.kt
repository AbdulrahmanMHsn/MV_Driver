package com.seamlabs.mvpdriver.notifications

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.seamlabs.mvpdriver.MainActivity
import com.seamlabs.mvpdriver.R
import com.seamlabs.mvpdriver.common.base.BaseFragment
import com.seamlabs.mvpdriver.databinding.FragmentNotificationsBinding


class NotificationsFragment : BaseFragment<FragmentNotificationsBinding>() {


    // Adapters
    private lateinit var notificationsAdapter: NotificationsAdapter


    override fun getViewBinding(): FragmentNotificationsBinding {
        return FragmentNotificationsBinding.inflate(layoutInflater)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }


    private fun initView(){
        (activity as MainActivity).hideBottomNav()
        binding.appBarNotifications.setTitle(R.string.notifications_title)
        binding.appBarNotifications.useSecondActionButton(true, R.drawable.ic_back){
            findNavController().popBackStack()
        }
        initRecyclerViewMyRequests()
        fakeData()
    }


    private fun initRecyclerViewMyRequests() {
        notificationsAdapter = NotificationsAdapter()
        binding.rcVwNotifications.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = notificationsAdapter
        }
    }

    private fun fakeData() {
        val list = listOf("0","1","0","1","0","1","2","2")

        notificationsAdapter.submitList(list)
    }

}