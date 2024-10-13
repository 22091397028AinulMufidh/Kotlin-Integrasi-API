package com.example.homeactivity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.homeactivity.databinding.ActivityMainBinding
import com.example.homeactivity.ui.upcoming.EventAdapterActive
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
//    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var eventAdapter: EventAdapterActive

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate the layout using ViewBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup BottomNavigationView
        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_upcoming, R.id.navigation_finished
            )
        )
        navView.setupWithNavController(navController)

//        // Initialize RecyclerView and Adapter
//        eventAdapter = EventAdapter(listOf())
//        binding.recyclerView.layoutManager = LinearLayoutManager(this)
//        binding.recyclerView.adapter = eventAdapter
//
//        // Observe LiveData from ViewModel
//        mainViewModel.eventData.observe(this) { eventResponse ->
//            eventResponse?.listEvents?.let { events ->
//                // Update adapter with new events
//                eventAdapter.updateEvents(events.filterNotNull())
//            }
//        }
//
//        // Call function to fetch events from API
//        mainViewModel.getEvents()
    }
}

