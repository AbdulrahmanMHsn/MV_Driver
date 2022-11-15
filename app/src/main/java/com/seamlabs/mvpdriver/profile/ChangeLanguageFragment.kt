package com.seamlabs.mvpdriver.profile

import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.seamlabs.mvpdriver.MainActivity
import com.seamlabs.mvpdriver.R
import com.seamlabs.mvpdriver.common.base.BaseFragment
import com.seamlabs.mvpdriver.common.utility.UserPreferences
import com.seamlabs.mvpdriver.databinding.FragmentChangeLanguageBinding
import java.util.*


class ChangeLanguageFragment : BaseFragment<FragmentChangeLanguageBinding>() {

    override fun getViewBinding(): FragmentChangeLanguageBinding {
        return FragmentChangeLanguageBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setBtnListeners()
    }

    private fun initView() {
        (activity as MainActivity).hideBottomNav()
        binding.appBarChangeLanguage.setTitleString(getString(R.string.change_language_title))
        binding.appBarChangeLanguage.useBackButton(true){
            findNavController().popBackStack()
        }
        if (UserPreferences.getUserLanguage(requireContext()) == Constant.ENGLISH) {
            binding.rightCheckIconEnglish.visibility = View.VISIBLE
        } else {
            binding.rightCheckIconArabic.visibility = View.VISIBLE
        }
    }

    private fun setBtnListeners() {
        binding.englishLayout.setOnClickListener {
            binding.rightCheckIconEnglish.visibility = View.VISIBLE
            binding.rightCheckIconArabic.visibility = View.GONE
            UserPreferences.setUserLanguage(requireContext(), Constant.ENGLISH)
            val packageManager = requireActivity().packageManager
            appLanguage()
            val intent = packageManager.getLaunchIntentForPackage(requireActivity().packageName)
            val componentName = intent?.component
            val mainIntent = Intent.makeRestartActivityTask(componentName)
            requireActivity().recreate()
            requireActivity().finish()
            requireActivity().startActivity(mainIntent)
        }
        binding.arabicLayout.setOnClickListener {
            binding.rightCheckIconEnglish.visibility = View.GONE
            binding.rightCheckIconArabic.visibility = View.VISIBLE
            UserPreferences.setUserLanguage(requireContext(), Constant.ARABIC)
            appLanguage()
            val packageManager = requireActivity().packageManager
            val intent = packageManager.getLaunchIntentForPackage(requireActivity().packageName)
            val componentName = intent?.component
            val mainIntent = Intent.makeRestartActivityTask(componentName)
            requireActivity().recreate()
            requireActivity().finish()
            requireActivity().startActivity(mainIntent)

        }
    }

    private fun appLanguage() {
        val language = UserPreferences.getUserLanguage(requireContext())
        Log.d("language1", language)
        if (language == Constant.ARABIC) {
            val locale = Locale(Constant.ARABIC)
            val config = Configuration(
                requireActivity().resources.configuration
            )
            Locale.setDefault(locale)
            config.setLocale(locale)
            changeLanguage(config)
        } else {
            val locale = Locale(Constant.ENGLISH)
            val config = Configuration(
                requireActivity().resources.configuration
            )
            Locale.setDefault(locale)
            config.setLocale(locale)
            changeLanguage(config)
        }
    }

    private fun changeLanguage(config: Configuration) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            requireActivity().baseContext.createConfigurationContext(config)
        }
        else{
            requireActivity().baseContext.resources.updateConfiguration(
                config,
                requireActivity().baseContext.resources.displayMetrics
            )
        }
    }


}

object Constant{
        const val ARABIC = "ar"
        const val ENGLISH = "en"
}