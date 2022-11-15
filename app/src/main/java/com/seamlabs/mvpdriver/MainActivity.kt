package com.seamlabs.mvpdriver

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.seamlabs.mvpdriver.common.utility.UserPreferences
import com.seamlabs.mvpdriver.databinding.ActivityMainBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    // Declaration binding
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    // navigation
    private lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var graph: NavGraph


    override fun onCreate(savedInstanceState: Bundle?) {
//        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initNavigationComponent()
//        setupSplashScreen(splashScreen)
    }

    private fun setupSplashScreen(splashScreen: androidx.core.splashscreen.SplashScreen) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {

//            splashScreen.setKeepOnScreenCondition {
//                true
//            }

            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.CREATED) {
                    delay(3000)
                    if (UserPreferences.getFirstTimeOpenAppState(this@MainActivity)) {
                        if (UserPreferences.getLoginState(this@MainActivity)) {
                            initNavigationComponent(R.id.marketRequestFragment)
                        } else {
                            initNavigationComponent(R.id.loginFragment)
                        }
                    }
                    splashScreen.setKeepOnScreenCondition {
                        false
                    }
                }
            }
        }
    }

    private fun initNavigationComponent(startDestination:Int = R.id.splashScreenFragment) {
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        graph = navController.navInflater.inflate(R.navigation.nav_graph)
        graph.setStartDestination(startDestination)
        navController.graph = graph
        binding.bottomNav.setupWithNavController(navController)
        binding.bottomNav.setOnItemReselectedListener {
        }
    }


    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }


    fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
        window.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )
    }


    fun hideProgressBar() {
        binding.progressBar.visibility = View.GONE
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }


    fun hideBottomNav() {
        binding.bottomNav.visibility = View.GONE
    }


    fun hideKeyboard() {
        val inputMethodManager = this.getSystemService(
            INPUT_METHOD_SERVICE
        ) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(
            this.currentFocus?.windowToken, 0
        )
    }


    fun showBottomNav() {
        binding.bottomNav.visibility = View.VISIBLE
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}