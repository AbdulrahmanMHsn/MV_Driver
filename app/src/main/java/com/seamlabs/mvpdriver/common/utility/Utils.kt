package com.seamlabs.mvpdriver.common.utility

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.util.DisplayMetrics
import com.seamlabs.mvpdriver.profile.Constant
import java.util.*
import kotlin.math.ceil

object Utils {

    //boolean location changed visted
    @kotlin.jvm.JvmField
    var visted = false

    //shred image path cropped
    @kotlin.jvm.JvmField
    var SHared_image_path: String? = null

    @JvmStatic
    fun appLanguageAndScreenZoom(context: Context, language: String) {

        val displayMetrics: DisplayMetrics = context.resources.displayMetrics
        val snap = 20
        val exactDpi: Float = (displayMetrics.xdpi + displayMetrics.ydpi) / 2
        val dpi: Int = displayMetrics.densityDpi
        val targetDpi = (ceil((exactDpi / snap).toDouble()) * snap).toInt()
        val resources: Resources = context.resources
        val config: Configuration =resources.configuration

        if (dpi - exactDpi > snap) {
            displayMetrics.densityDpi = targetDpi
            config.densityDpi = targetDpi + 15
            displayMetrics.setTo(displayMetrics)
        }
        if (language == Constant.ARABIC) {
            val locale: Locale = Locale(Constant.ARABIC)
            Locale.setDefault(locale)
            config.setLocale(locale)
            config.fontScale = 0.91f
            config.setLocale(locale)
            resources.updateConfiguration(
                config,
                displayMetrics
            )
        } else {
            val locale: Locale = Locale(Constant.ENGLISH)
            Locale.setDefault(locale)
            config.setLocale(locale)
            config.fontScale = 0.91f
            config.setLocale(locale)
            resources.updateConfiguration(
                config,
                displayMetrics
            )
        }
    }
}