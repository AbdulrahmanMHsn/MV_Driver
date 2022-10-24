package com.seamlabs.mvpdriver.myDeal

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.seamlabs.mvpdriver.MainActivity
import com.seamlabs.mvpdriver.R
import com.seamlabs.mvpdriver.common.base.BaseFragment
import com.seamlabs.mvpdriver.common.base.BaseViewModel
import com.seamlabs.mvpdriver.common.utility.*
import com.seamlabs.mvpdriver.databinding.FragmentMyDealsBinding
import com.seamlabs.mvpdriver.models.TripModel


class MyDealsFragment : BaseFragment<FragmentMyDealsBinding>() {

    // Adapters
    private lateinit var myDealsAdapter: MyDealsAdapter

    // ViewModels
    private val dealsViewModel: DealsViewModel by viewModels()

    private val signalsHandler: (Signal) -> Unit = { signal ->
        when (signal) {
            is Load -> showProgress()
            is StopLoading -> hideProgress()
            is SomethingWentWrong.ErrorMessage -> makeToast(BaseViewModel.errorMessage)
            is SomethingWentWrong.ConnectionFailure -> makeToast(BaseViewModel.errorMessage)
            else -> {}

        }
    }

    override fun getViewBinding(): FragmentMyDealsBinding {
        return FragmentMyDealsBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        signalsReceiver.initializeSignalsReceiver(signalsHandler, dealsViewModel)
        initView()
        dealsViewModel.getMarketRequests(requireContext())
        subscribeData()
        btnListener()
    }


    private fun initView() {
        (activity as MainActivity).showBottomNav()
        binding.appBarMyDeals.setTitle(R.string.my_deals)
        binding.appBarMyDeals.useSecondActionButton(true, R.drawable.ic_notification) {
            findNavController().navigate(R.id.notificationsFragment)
        }
        initRecyclerViewMyRequests()
    }

    private fun btnListener() {
        binding.upcomingChip.setOnClickListener {
            if (dealsViewModel.upComingDealsList.isNullOrEmpty()){
                binding.layoutEmptyDeals.emptyDeals.visibility = View.VISIBLE
            }else{
                binding.layoutEmptyDeals.emptyDeals.visibility = View.GONE
            }
        }

        binding.pastChip.setOnClickListener {
            if (dealsViewModel.upComingDealsList.isNullOrEmpty()){
                binding.layoutEmptyDeals.emptyDeals.visibility = View.VISIBLE
            }else{
                binding.layoutEmptyDeals.emptyDeals.visibility = View.GONE
            }
        }
    }

    private fun initRecyclerViewMyRequests() {
        myDealsAdapter = MyDealsAdapter {
            showInfoRequestDialog(it)
        }
        binding.rcVwMyDeals.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = myDealsAdapter
        }
    }

    private fun subscribeData() {
        dealsViewModel.dealsLiveData.observe(viewLifecycleOwner) { deals ->

            binding.upcomingChip.text = "${getString(R.string.upcoming)} ${deals.upcomingDealsCount}"
            binding.upcomingChip.text = "${getString(R.string.past)} ${deals.pastDealsCount}"

            if (deals.deals.upcomingDeals.isNullOrEmpty()) {
                binding.layoutEmptyDeals.emptyDeals.visibility = View.VISIBLE
            } else {
                binding.layoutEmptyDeals.emptyDeals.visibility = View.GONE
            }
        }
    }

    private fun showInfoRequestDialog(trip:TripModel) {
        val dialog = InfoRequestDialogFragment(trip)
        dialog.isCancelable = false
        dialog.show(requireActivity().supportFragmentManager, "InfoDialog")
    }

}