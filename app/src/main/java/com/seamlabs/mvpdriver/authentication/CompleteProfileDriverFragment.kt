package com.seamlabs.mvpdriver.authentication

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import androidx.navigation.fragment.findNavController
import com.orhanobut.hawk.Hawk.count
import com.seamlabs.mvpdriver.MainActivity
import com.seamlabs.mvpdriver.R
import com.seamlabs.mvpdriver.common.base.BaseFragment
import com.seamlabs.mvpdriver.common.utility.NavigationHelper
import com.seamlabs.mvpdriver.databinding.FragmentCompleteProfileDriverBinding


class CompleteProfileDriverFragment : BaseFragment<FragmentCompleteProfileDriverBinding>() {

    override fun getViewBinding(): FragmentCompleteProfileDriverBinding {
        return FragmentCompleteProfileDriverBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).hideBottomNav()

        addVehicle()
        initView()

        binding.btnAddVehicle.setOnClickListener {
            addVehicle(true)

            val count = binding.layoutAddVehicle.childCount

            for (i in 0 until count) {
                val v = binding.layoutAddVehicle.getChildAt(i)
                    .findViewById<EditText>(R.id.edTxt_car_brand).text
                Log.i("TAGTAGTAG", "onViewCreated: $v")
                //do something with your child element
            }
        }

        binding.btnSave.setOnClickListener {
            NavigationHelper.navigateInclusive(findNavController(),
                R.id.completeProfileDriverFragment,
                R.id.marketRequestFragment)
        }
    }

    private fun initView() {
        binding.appBarCompleteProfileDriver.setTitle(R.string.complete_your_driver_profile)
        binding.appBarCompleteProfileDriver.useSecondActionButton(true, R.drawable.ic_back){
            findNavController().popBackStack()
        }
    }



    private fun addVehicle(isAdded: Boolean = false) {
        val child = LayoutInflater.from(requireContext())
            .inflate(R.layout.layout_section_add_vehicle, null)
        val brand = child.findViewById<EditText>(R.id.edTxt_car_brand)
        val model = child.findViewById<EditText>(R.id.edTxt_car_model)
        val manufacturingYear = child.findViewById<EditText>(R.id.edTxt_car_manufacturing_year)
        val line = child.findViewById<View>(R.id.view_line)

        if (isAdded) {
            line.visibility = View.VISIBLE
        }

        Log.i("TAGTAGTAG", "addVehicle: ${brand.text}")

        binding.layoutAddVehicle.addView(child)
    }


}