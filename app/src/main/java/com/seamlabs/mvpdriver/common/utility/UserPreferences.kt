package com.seamlabs.mvpdriver.common.utility

import android.content.Context
import android.content.SharedPreferences

object UserPreferences {


    private fun getUserSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences("SHARED_USER_PROFILE", Context.MODE_PRIVATE)
    }


    fun setLoginState(context: Context, loggedIn: Boolean) {
        val editor: SharedPreferences.Editor = getUserSharedPreferences(context).edit()
        editor.putBoolean("SHARED_USER_LOGIN_STATE", loggedIn)
        editor.apply()
    }


    fun getLoginState(context: Context): Boolean {
        return getUserSharedPreferences(context).getBoolean("SHARED_USER_LOGIN_STATE", false)
    }


    fun setFirstTimeOpenAppState(context: Context, isFirstInstalled: Boolean) {
        val editor: SharedPreferences.Editor = getUserSharedPreferences(context).edit()
        editor.putBoolean("SHARED_FIRST_INSTALLED_STATE", isFirstInstalled)
        editor.apply()
    }


    fun getFirstTimeOpenAppState(context: Context): Boolean {
        return getUserSharedPreferences(context).getBoolean("SHARED_FIRST_INSTALLED_STATE", false)
    }


    fun storeToken(context: Context, apiKey: String) {
        val editor: SharedPreferences.Editor = getUserSharedPreferences(context).edit()
        editor.putString("Token", apiKey)
        editor.apply()
    }


    fun getToken(context: Context): String {
        return getUserSharedPreferences(context).getString("Token", null).toString()
    }


    fun setUserLanguage(context:Context,lang:String) {
        val settings:SharedPreferences = context . getSharedPreferences ("App Settings", Context.MODE_PRIVATE)
        settings.edit().putString("SHARED_USER_LANGUAGE", lang).apply()

    }


    fun getUserLanguage(context:Context): String {
        return context.getSharedPreferences("App Settings",Context.MODE_PRIVATE).getString("SHARED_USER_LANGUAGE", "en").toString()
    }

}