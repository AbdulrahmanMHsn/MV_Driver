package com.seamlabs.mvpdriver.common.base


import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.viewbinding.ViewBinding
import artifact.signals_bus.SignalsReceiver
import com.seamlabs.mvpdriver.MainActivity
import com.seamlabs.mvpdriver.common.utility.Signal

import kotlin.math.round


abstract class BaseFragment <T : ViewBinding> : Fragment() {

    // Declaration Binding
    private var _binding: T? = null
    val binding get() = _binding!!


    lateinit var signalsReceiver: SignalsReceiver<Signal>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = getViewBinding()
        return binding.root
    }

    /**
     * Override for set view
     *
     * @return view
     */
    abstract fun getViewBinding(): T


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    var GBS_Enabled = false
    var networkEnabled = false
    private var locationManager: LocationManager? = null

    private val permissionsResults =MutableLiveData<Map<String,Boolean>?>()
    val permissionsResultsLive = permissionsResults

     val activityResultsData =MutableLiveData<Intent?>()
    val activityResultsDataLive = activityResultsData

    var  requestPermissionsCallBack= registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) {
        permissionsResults.value = it
    }

    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            activityResultsData.value = data
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        signalsReceiver = SignalsReceiver(viewLifecycleOwner)
    }

    override fun onStop() {
        super.onStop()
        permissionsResults.value = null
        activityResultsData.value = null
    }

    override fun onDestroy() {
        super.onDestroy()
        requestPermissionsCallBack.unregister()
        resultLauncher.unregister()

    }


    protected fun launchPermission (permissions:Array<String>){
        try {
            requestPermissionsCallBack.launch(permissions)
        }
        catch (ex:Exception){
            //
        }
    }

    protected fun launchActivityForResult (intent:Intent){
        try {
            resultLauncher.launch(intent)
        }
        catch (ex:Exception){
        Log.d("myDebug","BaseFragment launchActivityForResult   " + ex.localizedMessage )
        }
    }



    protected fun isPermissionGranted(permission: String): Boolean {

        return  try{
            ContextCompat.checkSelfPermission(
                requireContext(),
                permission
            ) == PackageManager.PERMISSION_GRANTED
        }
        catch (ex:Exception){
           return false
        }

    }


    protected fun showProgress(){
        (activity as MainActivity).showProgressBar()
    }

    protected fun hideProgress(){
        (activity as MainActivity).hideProgressBar()
    }

//    protected fun checkLocationState(): Task<LocationSettingsResponse> {
//        val builder =
//            LocationSettingsRequest.Builder()
//                .addLocationRequest(LocationRequest.create().apply {
//                    priority = LocationRequest.PRIORITY_HIGH_ACCURACY
//                })
//        val client = LocationServices.getSettingsClient(requireContext())
//        return client.checkLocationSettings(builder.build())
//    }

    protected fun makeToast(text: String) {
        try {
            Toast.makeText(requireContext(), text, Toast.LENGTH_LONG).show()
        }
        catch (ex:Exception){
        }
    }

//    protected fun showLoadingIndicator() {
//        (requireActivity() as UpdateNavigationComponents).showProgressBar()
//    }
//
//    protected fun hideLoadingIndicator() {
//        (requireActivity() as UpdateNavigationComponents).hideProgressBar()
//    }

    fun Double.round(decimals: Int): Double {
        var multiplier = 1.0
        repeat(decimals) { multiplier *= 10 }
        return round(this * multiplier) / multiplier
    }


//    fun showExplanation(
//        title: String,
//        message: String,
//        permissions: Array<String>,
//    ) {
//        try {
//            val builder = AlertDialog.Builder(
//                requireContext(),
//            )
//            builder.setTitle(title)
//                .setMessage(message)
//                .setPositiveButton(android.R.string.ok) { dialog, id ->
//                    //  launchPermission(permissions)
//                    makeToast(getString(R.string.permission_denied))
//                    dialog.dismiss()
//                }
//            builder.create().show()
//        }
//        catch (ex:Exception){
//            //
//        }
//
//
//    }


//    private fun showSnackBar() {
//        snackbar = Snackbar.make(
//            requireView(),
//            requireContext().resources.getString(R.string.no_internet),
//            Snackbar.LENGTH_INDEFINITE
//        ).setAction(getString(R.string.retry)) { v: View? ->/* checkNetwork() */ }
//            .setActionTextColor(requireContext().resources.getColor(R.color.yellow))
//        val snackBarView = snackbar!!.view
//        val snackBarText =
//            snackBarView.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
//        snackBarText.setTextColor(requireContext().resources.getColor(R.color.white))
//        if (this.isVisible)
//            snackbar!!.show()
//    }

//    protected fun checkGpsEnabled(): Boolean {
//        locationManager =
//            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
//        try {
//            GBS_Enabled = locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
//        } catch (ex: Exception) {
//        }
//        try {
//            networkEnabled = locationManager!!.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
//        } catch (ex: Exception) {
//        }
//        if (!GBS_Enabled && !networkEnabled) {
//            // notify user
//            AlertDialog.Builder(requireContext())
//                .setMessage(R.string.no_gps_profile)
//                .setPositiveButton(R.string.open_location_settings) { paramDialogInterface, paramInt ->
//                    startActivity(
//                        Intent(
//                            Settings.ACTION_LOCATION_SOURCE_SETTINGS
//                        )
//                    )
//                    paramDialogInterface.dismiss()
//                }
//                .setNegativeButton(R.string.cancel, null)
//                .show()
//            return false
//        } else return true
//    }



//    fun isValidPassword(password: String): Boolean {
//        val pattern: Pattern =
//            Pattern.compile(Constant.PASSWORD_REGEX_PATTERN)
//        val matcher: Matcher = pattern.matcher(password)
//        return matcher.matches()
//    }
//
//    fun isEightChar(password: String): Boolean {
//        val pattern: Pattern =
//            Pattern.compile(Constant.MINIMUM_EIGHT_LENGTH)
//        val matcher: Matcher = pattern.matcher(password)
//        return matcher.lookingAt()
//    }
//
//    fun containUpperCase(password: String): Boolean {
//        val pattern: Pattern =
//            Pattern.compile(Constant.AT_LEAST_UPPERCASE)
//        val matcher: Matcher = pattern.matcher(password)
//        return matcher.lookingAt()
//    }
//
//    fun containLowerCase(password: String): Boolean {
//        val pattern: Pattern =
//            Pattern.compile(Constant.AT_LEAST_LOWERCASE)
//        val matcher: Matcher = pattern.matcher(password)
//        return matcher.lookingAt()
//    }
//
//
//    fun containOneDigit(password: String): Boolean {
//        val pattern: Pattern =
//            Pattern.compile(Constant.AT_LEAST_ONE_DIGIT)
//        val matcher: Matcher = pattern.matcher(password)
//        return matcher.lookingAt()
//    }
}

