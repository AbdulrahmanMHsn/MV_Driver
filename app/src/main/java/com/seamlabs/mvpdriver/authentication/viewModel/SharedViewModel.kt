package com.seamlabs.mvpdriver.authentication.viewModel

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.seamlabs.mvpdriver.R
import com.seamlabs.mvpdriver.common.base.BaseViewModel
import com.seamlabs.mvpdriver.common.utility.*
import kotlinx.coroutines.launch
import org.json.JSONException
import java.io.IOException


class SharedViewModel(application: Application) : BaseViewModel(application) {

    private val _gender:MutableLiveData<Pair<String,String>?> = MutableLiveData(null)
    val gender:LiveData<Pair<String,String>?> = _gender

    fun setGender(gender:Pair<String,String>){
        _gender.value = gender
    }


    private val _vehicleType:MutableLiveData<Pair<String,String>?> = MutableLiveData(null)
    val vehicleType:LiveData<Pair<String,String>?> = _vehicleType

    fun setVehicleType(vehicleType:Pair<String,String>){
        _vehicleType.value = vehicleType
    }


    fun clear(){
        _gender.value = null
        _vehicleType.value = null
    }

}