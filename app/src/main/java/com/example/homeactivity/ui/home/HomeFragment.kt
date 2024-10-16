package com.example.homeactivity.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.homeactivity.databinding.FragmentHomeBinding
import com.example.homeactivity.ui.upcoming.EventAdapterActive

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var adapter: EventAdapterActive
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment using ViewBinding
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize ViewModel
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        // Setup RecyclerView
        adapter = EventAdapterActive(emptyList()) { selectedEvent ->
            // Handle item click and navigate to UpcomingDetailActivity
        }
//        binding.recyclerHome.adapter = adapter
//        binding.recyclerHome.layoutManager = LinearLayoutManager(requireContext())

        // Tampilkan ProgressBar
        binding.progressBar.visibility = View.VISIBLE

        // Observe LiveData from ViewModel for events
        homeViewModel.eventData.observe(viewLifecycleOwner) { eventResponse ->
            eventResponse?.let {
                val events = it.listEvents ?: emptyList()
                if (events.isNotEmpty()) {
                    adapter.updateData(events)
                } else {
                    // Handle case when events list is empty
                    Toast.makeText(requireContext(), "No events found", Toast.LENGTH_SHORT).show()
                }
            }
            // Sembunyikan ProgressBar setelah data selesai dimuat
            binding.progressBar.visibility = View.GONE
        }

        // Fetch events when the fragment is created
        homeViewModel.getEventsActive()
    }
}