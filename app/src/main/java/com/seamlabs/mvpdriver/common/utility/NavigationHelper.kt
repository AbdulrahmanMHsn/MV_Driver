package com.seamlabs.mvpdriver.common.utility

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.seamlabs.mvpdriver.R

object NavigationHelper {

    fun navigateInclusive(
        navController: NavController,
        @IdRes sourceId: Int,
        @IdRes destinationId: Int,
        args: Bundle?= null,
    ) {
        val navOption =
            NavOptions.Builder().apply {
                setPopUpTo(sourceId, true)
                setLaunchSingleTop(true)
                setEnterAnim(R.anim.slide_in_right)
                setExitAnim(R.anim.slide_out_left)
                setPopEnterAnim(R.anim.slide_in_left)
                setPopExitAnim(R.anim.slide_out_right)
            }.build()
        navController.navigate(destinationId, args, navOption)
    }

    fun navigate(
        navController: NavController,
        @IdRes sourceId: Int,
        @IdRes destinationId: Int,
        args: Bundle? = null,
    ) {
        val navOption =
            NavOptions.Builder().apply {
                setPopUpTo(sourceId, false)
                setLaunchSingleTop(true)
                setEnterAnim(R.anim.slide_in_right)
                setPopEnterAnim(R.anim.slide_in_left)
                setPopExitAnim(R.anim.slide_out_right)
            }.build()
        navController.navigate(destinationId, args, navOption)
    }

}