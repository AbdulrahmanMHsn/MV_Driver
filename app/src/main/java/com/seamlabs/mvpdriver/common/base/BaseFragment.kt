package com.seamlabs.mvpdriver.common.base


import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.viewbinding.ViewBinding
import artifact.signals_bus.SignalsReceiver
import com.seamlabs.mvpdriver.BuildConfig
import com.seamlabs.mvpdriver.MainActivity
import com.seamlabs.mvpdriver.R
import com.seamlabs.mvpdriver.common.utility.ImageHelper
import com.seamlabs.mvpdriver.common.utility.PermissionRequest
import com.seamlabs.mvpdriver.common.utility.Signal
import io.reactivex.internal.subscriptions.SubscriptionHelper.cancel
import java.io.File

import kotlin.math.round


abstract class BaseFragment<T : ViewBinding> : Fragment() {

    // Declaration Binding
    private var _binding: T? = null
    val binding get() = _binding!!


    lateinit var signalsReceiver: SignalsReceiver<Signal>

    private var _imageHelper: ImageHelper? = null
    private val imageHelper get() = _imageHelper!!

    private var currentImgPath: String? = null
    private var currentOriginalImgPath: String? = null

    private var _currentImagePath = MutableLiveData<String?>()
    val currentImagePathLiveData = _currentImagePath

    val storagePermissions = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA
    )

    fun setCurrentImagePath(path:String?){
        _currentImagePath.value = path
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
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
        imageHelper.clearMyFiles(currentOriginalImgPath)
        _imageHelper = null
        currentImgPath = null
        _binding = null
    }


    var GBS_Enabled = false
    var networkEnabled = false
    private var locationManager: LocationManager? = null

    private val permissionsResults = MutableLiveData<Map<String, Boolean>?>()
    val permissionsResultsLive = permissionsResults

    val activityResultsData = MutableLiveData<Intent?>()
    val activityResultsDataLive = activityResultsData

    var requestPermissionsCallBack = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) {
        permissionsResults.value = it
    }

    var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                activityResultsData.value = data
            }
        }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        signalsReceiver = SignalsReceiver(viewLifecycleOwner)
        _imageHelper = ImageHelper(requireContext())
        subscribePermissions()

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

    fun performCameraAndGalleyAction() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val uri = getTmpFileUri()
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        val chooserIntent = Intent(Intent.ACTION_CHOOSER)
        chooserIntent.putExtra(Intent.EXTRA_INTENT, galleryIntent)
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(cameraIntent))
        chooserIntent.putExtra(Intent.EXTRA_TITLE, "Select from:");
        resultLauncher.launch(chooserIntent)
    }


    private fun getTmpFileUri(): Uri {
        val tmpFile =
            File.createTempFile("tmp_image_file", ".png", requireContext().cacheDir).apply {
                createNewFile()
                deleteOnExit()
            }
        currentImgPath = tmpFile.absolutePath
        currentOriginalImgPath = tmpFile.name
        return FileProvider.getUriForFile(
            requireContext().applicationContext,
            "${BuildConfig.APPLICATION_ID}.provider",
            tmpFile
        )
    }

    protected fun launchPermission(permissions: Array<String>) {
        try {
            requestPermissionsCallBack.launch(permissions)
        } catch (ex: Exception) {
            //
        }
    }

    protected fun launchActivityForResult(intent: Intent) {
        try {
            resultLauncher.launch(intent)
        } catch (ex: Exception) {
            Log.d("myDebug", "BaseFragment launchActivityForResult   " + ex.localizedMessage)
        }
    }

    fun handleProfilePictureChange() {
        if (isPermissionGranted(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            performCameraAndGalleyAction()
        } else {
            launchPermission(storagePermissions)
        }
    }

//    fun performCameraAndGalleyAction() {
//        val i = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//        launchActivityForResult(i)
//    }

    private fun subscribeActivityResult() {
        activityResultsDataLive.observe(viewLifecycleOwner) {
            if (it != null) {
                try {
                    if (it.data != null) {
                        val selectedImage = it.data
                        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                        val cursor =
                            requireActivity().contentResolver.query(
                                selectedImage!!,
                                filePathColumn,
                                null,
                                null,
                                null
                            )
                        if (cursor != null) {
                            try {
                                val columnIndex = cursor.getColumnIndexOrThrow(filePathColumn[0])

                                cursor.moveToFirst()

                                val imagePath = cursor.getString(columnIndex)

                                val currentImgPath = imageHelper.compressImage(imagePath)
                                _currentImagePath.postValue(currentImgPath)

                            } catch (ex: java.lang.Exception) {
                                makeToast(getString(R.string.someThing_went_wrong))
                            }
                            cursor.close()
                        } else {
                            makeToast(getString(R.string.someThing_went_wrong))

                        }
                    } else makeToast(getString(R.string.someThing_went_wrong))

                } catch (ex: Exception) {
                    makeToast(getString(R.string.someThing_went_wrong))
                }
            } else {
                currentImgPath?.let { path ->
                    val bitmap = BitmapFactory.decodeFile(path)
                    if (bitmap != null) {
                        _currentImagePath.postValue(imageHelper.compressImage(path))
                    }
                }
            }
        }
    }

    internal fun subscribePermissions() {
        permissionsResultsLive.observe(viewLifecycleOwner) {
            it?.keys.apply {
                this?.forEach { st ->
                    when (st) {
                        Manifest.permission.READ_EXTERNAL_STORAGE -> {
                            if (it?.get(Manifest.permission.READ_EXTERNAL_STORAGE) == true) {
                                performCameraAndGalleyAction()
                            } else {
                                showExplanation(
                                    getString(R.string.permission_needed),
                                    getString(R.string.picture_permission_explanation),
                                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                                )
                            }
                        }
                        Manifest.permission.ACCESS_FINE_LOCATION -> {
                            if (it?.get(Manifest.permission.ACCESS_FINE_LOCATION) == true) {
                                if (checkGpsEnabled()) {
                                    if (ActivityCompat.checkSelfPermission(requireContext(),
                                            Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                                            requireContext(),
                                            Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                                    ) {
                                        return@forEach
                                    }
                                }
                            } else {
                                try {
                                    showExplanation(
                                        getString(R.string.permission_denied),
                                        getString(R.string.no_gps),
                                        PermissionRequest.locationPermissions
                                    )
                                } catch (ex: Exception) {

                                }

                            }
                        }
                    }
                }
            }
        }
    }


    protected fun isPermissionGranted(permission: String): Boolean {

        return try {
            ContextCompat.checkSelfPermission(
                requireContext(),
                permission
            ) == PackageManager.PERMISSION_GRANTED
        } catch (ex: Exception) {
            return false
        }

    }


    protected fun showProgress() {
        (activity as MainActivity).showProgressBar()
    }

    protected fun hideProgress() {
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
        } catch (ex: Exception) {
        }
    }

    fun EditText.validateEmail(): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(this.text.toString()).matches()
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


    fun showExplanation(
        title: String,
        message: String,
        permissions: Array<String>,
    ) {
        try {
            val builder = AlertDialog.Builder(
                requireContext(),
            )
            builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok) { dialog, id ->
//                      launchPermission(permissions)
                    makeToast(getString(R.string.permission_denied))
                    dialog.dismiss()
                }
            builder.create().show()
        } catch (ex: Exception) {
            //
        }


    }


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

    protected fun checkGpsEnabled(): Boolean {
        locationManager =
            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        try {
            GBS_Enabled = locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
        } catch (ex: Exception) {
        }
        try {
            networkEnabled = locationManager!!.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        } catch (ex: Exception) {
        }
        if (!GBS_Enabled && !networkEnabled) {
            // notify user
            AlertDialog.Builder(requireContext())
                .setMessage(R.string.no_gps_profile)
                .setPositiveButton(R.string.open_location_settings) { paramDialogInterface, paramInt ->
                    startActivity(
                        Intent(
                            Settings.ACTION_LOCATION_SOURCE_SETTINGS
                        )
                    )
                    paramDialogInterface.dismiss()
                }
                .setNegativeButton(R.string.cancel, null)
                .show()
            return false
        } else return true
    }


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

