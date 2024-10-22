package com.example.homeactivity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.homeactivity.databinding.ActivityMainBinding
import com.example.homeactivity.ui.setting.MainViewModel
import com.example.homeactivity.ui.setting.SettingPreferences
import com.example.homeactivity.ui.setting.ViewModelFactory
import com.example.homeactivity.ui.setting.dataStore
import com.example.homeactivity.ui.upcoming.EventAdapterActive
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
//    private var mainViewModel: MainViewModel by viewModels()
    private lateinit var mainViewModel: MainViewModel
    private lateinit var eventAdapter: EventAdapterActive

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate the layout using ViewBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//       DARK THEME START
        // Initialize DataStore and ViewModel
        val pref = SettingPreferences.getInstance(application.dataStore)
        mainViewModel = ViewModelProvider(this, ViewModelFactory(pref)).get(MainViewModel::class.java)

        // Observe the theme setting and apply it
        mainViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
//        END DARK THEME

        // Setup BottomNavigationView
        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_upcoming, R.id.navigation_finished,
                R.id.navigation_favorite, R.id.navigation_setting
            )
        )
        navView.setupWithNavController(navController)

    }
}

