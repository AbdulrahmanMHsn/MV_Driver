package com.seamlabs.mvpdriver.marketRequests

import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.seamlabs.mvpdriver.MainActivity
import com.seamlabs.mvpdriver.R
import com.seamlabs.mvpdriver.common.base.BaseFragment
import com.seamlabs.mvpdriver.common.base.BaseViewModel
import com.seamlabs.mvpdriver.common.utility.*
import com.seamlabs.mvpdriver.databinding.FragmentMarketRequestBinding
import com.seamlabs.mvpdriver.marketRequests.viewModel.MarketRequestViewModel
import com.seamlabs.mvpdriver.models.TripModel
import com.seamlabs.mvpdriver.models.TripType


class MarketRequestFragment : BaseFragment<FragmentMarketRequestBinding>() {

    // Adapters
    private lateinit var marketRequestsAdapter: MarketRequestsAdapter

    // Views
    // BottomSheetDialog
    private lateinit var submitOfferBottomSheet: BottomSheetBehavior<ConstraintLayout>

    // ViewModels
    private val marketRequestViewModel: MarketRequestViewModel by viewModels()


    private val signalsHandler: (Signal) -> Unit = { signal ->
        when (signal) {
            is Load -> showProgress()
            is StopLoading -> hideProgress()
            is SomethingWentWrong.ErrorMessage -> makeToast(BaseViewModel.errorMessage)
            is SomethingWentWrong.ConnectionFailure -> makeToast(BaseViewModel.errorMessage)
            is Navigate.OnSuccessSubmitOffer -> {
                findNavController().navigate(R.id.successfullySubmittedOfferFragment)
            }
            else -> {}

        }
    }


    override fun getViewBinding(): FragmentMarketRequestBinding {
        return FragmentMarketRequestBinding.inflate(layoutInflater)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        signalsReceiver.initializeSignalsReceiver(signalsHandler, marketRequestViewModel)
        initView()
        marketRequestViewModel.getMarketRequests(requireContext())
        subscribeData()
    }


    private fun initView() {
        (activity as MainActivity).showBottomNav()
        binding.appBarMarketRequest.setTitle(R.string.market_request)
        binding.appBarMarketRequest.useSecondActionButton(true, R.drawable.ic_notification) {
            findNavController().navigate(R.id.notificationsFragment)
        }
        initRecyclerViewMyRequests()
    }


    private fun initRecyclerViewMyRequests() {
        marketRequestsAdapter = MarketRequestsAdapter {
            initSubmitOfferBottomSheetDialog(it)
        }
        binding.rcVwMarketRequests.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = marketRequestsAdapter
        }
    }


    private fun subscribeData() {
        marketRequestViewModel.marketRequestsLiveData.observe(viewLifecycleOwner) { list ->
            if (list.isNullOrEmpty()) {
                 binding.layoutEmptyMarkets.emptyMarkets.visibility = View.VISIBLE
            }else{
                binding.layoutEmptyMarkets.emptyMarkets.visibility = View.GONE
                marketRequestsAdapter.submitList(list)
            }
        }
    }


    private fun initSubmitOfferBottomSheetDialog(trip: TripModel) {
        submitOfferBottomSheet =
            BottomSheetBehavior.from(binding.dialogSubmitOffer.dialogSubmitOfferBottomSheet)
        submitOfferBottomSheet.state = BottomSheetBehavior.STATE_HALF_EXPANDED
        submitOfferBottomSheet.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
            override fun onStateChanged(bottomSheet: View, newState: Int) {

                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    (activity as MainActivity).showBottomNav()
                } else {
                    (activity as MainActivity).hideBottomNav()
                }
            }
        })

        binding.dialogSubmitOffer.txtNoOfStudents.text = "${trip.boysCount}" + "${trip.girlsCount}"

        binding.dialogSubmitOffer.txtDistanceTrips.text = "1.5 KM"

        trip.goTripTime?.let {
            binding.dialogSubmitOffer.txtDateStartRequest.text = it
        }

        trip.backTripTime?.let {
            binding.dialogSubmitOffer.txtDateEndRequest.text = it
        }

        when (trip.tripType) {
            TripType.GO_TRIP.name -> {
                binding.dialogSubmitOffer.txtNoOfTrips.text = "1"
                binding.dialogSubmitOffer.txtTypeTrip.text = getString(R.string.go_trip)
            }
            TripType.BACK_TRIP.name -> {
                binding.dialogSubmitOffer.txtNoOfTrips.text = "1"
                binding.dialogSubmitOffer.txtTypeTrip.text = getString(R.string.back_trip)
            }
            TripType.ROUND_TRIP.name -> {
                binding.dialogSubmitOffer.txtNoOfTrips.text = "2"
                binding.dialogSubmitOffer.txtTypeTrip.text = getString(R.string.round_trip)
            }
        }

        binding.dialogSubmitOffer.btnSubmitOffer.setOnClickListener {
            val price = binding.dialogSubmitOffer.edTxtValueOffer.text.toString().trim()
            val comment = binding.dialogSubmitOffer.edTxtComment.text.toString().trim()

            if (price.isEmpty()) {
                binding.dialogSubmitOffer.edTxtValueOffer.error = getString(R.string.empty_field)
            } else {
                marketRequestViewModel.submitOffer(requireContext(),
                    trip.id,
                    price.toInt(),
                    comment)
            }
        }

    }

}