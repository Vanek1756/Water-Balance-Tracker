package com.babiichuk.waterbalancetracker.app.ui.pages.main

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import com.babiichuk.waterbalancetracker.R
import com.babiichuk.waterbalancetracker.app.ui.extensions.show
import com.babiichuk.waterbalancetracker.app.ui.extensions.showOrHideIf
import com.babiichuk.waterbalancetracker.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val viewModel: MainActivityViewModel by viewModels()

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.mainLayout) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fcvMain) as NavHostFragment
        navController = navHostFragment.navController

        setupBottomNavMenu()
        binding.setupBinding()
        viewModel.subscribe()
    }

    private fun startHomeFragments() {
        binding.bottomNavView.show()
        val navOptions = NavOptions.Builder()
            .setPopUpTo(R.id.nav_poll, false)
            .build()
        navController.navigate(R.id.nav_main, null, navOptions)
    }

    private fun setupBottomNavMenu() {
        val navOptions = NavOptions.Builder()
            .setPopUpTo(R.id.homeFragment, false)
            .build()
        binding.bottomNavView.onItemClickListener = { menuId ->
            navController.navigate(menuId, null, navOptions)
        }
    }


    private fun ActivityMainBinding.setupBinding() {

    }

    private fun MainActivityViewModel.subscribe() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch { userIsExistFlow.collect { if (it) startHomeFragments() } }
                launch { showWaterRateScreenFlow.collect { openRateDrunkFragment(it) } }
            }
        }
    }

    private fun openRateDrunkFragment(value: Boolean) {
        if (!value) return
        navController.navigate(R.id.action_global_rateDrunk)
    }

    fun showOrHideBottomNavigationMenu(value: Boolean){
        binding.bottomNavView.showOrHideIf { value }
    }

}