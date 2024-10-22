package com.example.homeactivity.ui.upcoming

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.text.HtmlCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.homeactivity.R
import com.example.homeactivity.data.response.ListEventsItem
import com.example.homeactivity.databinding.ActivityUpcomingDetailBinding
import com.example.homeactivity.ui.setting.MainViewModel
import com.example.homeactivity.ui.setting.SettingPreferences
import com.example.homeactivity.ui.setting.ViewModelFactory
import com.example.homeactivity.ui.setting.dataStore

class UpcomingDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpcomingDetailBinding
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate the layout using ViewBinding
        binding = ActivityUpcomingDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // DARK THEME START
        // Initialize DataStore and ViewModel
        val pref = SettingPreferences.getInstance(application.dataStore) // Initialize DataStore
        mainViewModel = ViewModelProvider(this, ViewModelFactory(pref)).get(MainViewModel::class.java) // Initialize ViewModel

        // Observe the theme setting and apply it
        mainViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
        // DARK THEME END

        // Retrieve event data from intent
        val eventItem: ListEventsItem? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("EVENT_DATA", ListEventsItem::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("EVENT_DATA")
        }

        // Display event data if available
        eventItem?.let {
            binding.tvEventName.text = it.name
            binding.tvOwnerName.text = it.ownerName
            binding.tvEventTime.text = it.beginTime
            val remainingQuota = it.quota?.minus(it.registrants ?: 0)
            binding.tvEventQuota.text = "Sisa Kuota $remainingQuota"
            binding.tvEventDescription.text = HtmlCompat.fromHtml(
                it.description ?: "No Description Available",
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )

            // Button Register
            binding.btnRegist.setOnClickListener {
                val url = "https://www.dicoding.com/events/"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
            }

            // Display image using Glide
            Glide.with(this)
                .load(it.mediaCover)
                .placeholder(R.drawable.ic_launcher_background)
                .into(binding.imgEventLogo)
        } ?: run {
            // Handle if eventItem is null
            binding.tvEventName.text = "Event Not Found"
        }
    }
}



