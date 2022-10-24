package com.seamlabs.mvpdriver.common.utility

import android.Manifest

object PermissionRequest {
    const val LOCATION_SETTINGS_CHECK_REQUEST = 0
    val locationPermissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION
    )

}