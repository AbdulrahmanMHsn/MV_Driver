package com.seamlabs.mvpdriver.authentication.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.seamlabs.mvpdriver.R
import com.seamlabs.mvpdriver.authentication.viewModel.CompleteProfileViewModel
import com.seamlabs.mvpdriver.common.base.BaseFragment
import com.seamlabs.mvpdriver.common.utility.LocationHelper
import com.seamlabs.mvpdriver.common.utility.PermissionRequest
import com.seamlabs.mvpdriver.databinding.FragmentMapBinding


class MapFragment : BaseFragment<FragmentMapBinding>() {

    private var map: GoogleMap? = null
    private val vm:CompleteProfileViewModel by activityViewModels()


    @SuppressLint("MissingPermission")
    private val callback = OnMapReadyCallback { googleMap ->
        map = googleMap
        if (isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)) {
            map?.isMyLocationEnabled = false
            if (checkGpsEnabled()) {
                getCurrentLocation()
            }
        } else {
            launchPermission(PermissionRequest.locationPermissions)
        }

    }


    override fun getViewBinding(): FragmentMapBinding {
        return FragmentMapBinding.inflate(layoutInflater)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        initView()
        subscribePermissions()
        btnListener()
    }


    private fun initView(){
        binding.appBarMap.setTitle(R.string.preferred_working_area)
        binding.appBarMap.useSecondActionButton(true, R.drawable.ic_back){
            findNavController().popBackStack()
        }
    }


    private fun btnListener() {
        binding.fabFindMyLocation.setOnClickListener {
            if (checkGpsEnabled()){
                getCurrentLocation()
            }
        }

        binding.saveLocationMapFragment.setOnClickListener {
            val lat = map?.cameraPosition?.target?.latitude
            val lng = map?.cameraPosition?.target?.longitude

            if (lat != null && lng != null) {
                vm.setPreferredArea(LatLng(lat, lng))
            }

           findNavController().popBackStack()

        }
    }


    private fun getCurrentLocation() {

        LocationHelper(requireActivity()) {
            if (it != null) {
                try {
                    map?.animateCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            LatLng(it.latitude, it.longitude),
                            15f
                        )
                    )
                    vm.setPreferredArea(LatLng(it.latitude,it.longitude))
                }catch (ex:Exception){
                    Log.i("TAGTAGTAG", "getCurrentLocation: $ex")
                }

            }
        }

    }


//    private fun subscribePermissions() {
//        permissionsResultsLive.observe(viewLifecycleOwner) {
//            it?.keys.apply {
//                this?.forEach { st ->
//                    when (st) {
//                        Manifest.permission.ACCESS_FINE_LOCATION -> {
//                            if (it?.get(Manifest.permission.ACCESS_FINE_LOCATION) == true) {
//                                if (checkGpsEnabled()) {
//                                    if (ActivityCompat.checkSelfPermission(requireContext(),
//                                            Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                                            requireContext(),
//                                            Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
//                                    ) {
//                                        return@forEach
//                                    }
//                                    map?.isMyLocationEnabled = true
//                                    getCurrentLocation()
//                                }
//                            } else {
//                                try {
//                                    showExplanation(
//                                        getString(R.string.permission_denied),
//                                        getString(R.string.no_gps),
//                                        PermissionRequest.locationPermissions
//                                    )
//                                } catch (ex: Exception) {
//
//                                }
//
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }


    override fun onPause() {
        super.onPause()
        LocationHelper(requireActivity()).stopLocation()
    }

}