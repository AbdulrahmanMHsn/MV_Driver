package com.seamlabs.mvpdriver.common.utility

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.fragment.app.Fragment


fun Activity.callIntent(phoneNumber:String){
    val intent = Intent(
        Intent.ACTION_DIAL,
        Uri.parse("tel:$phoneNumber")
    )
    startActivity(intent)
}