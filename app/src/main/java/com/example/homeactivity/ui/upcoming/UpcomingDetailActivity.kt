package com.example.homeactivity.ui.upcoming

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.homeactivity.R
import com.example.homeactivity.data.entity.FavoriteEvent
import com.example.homeactivity.data.response.ListEventsItem
import com.example.homeactivity.databinding.ActivityMainBinding
import com.example.homeactivity.ui.favorite.insert.FavoriteEventAddUpdateViewModel
import com.example.homeactivity.ui.setting.SettingPreferences
import com.example.homeactivity.ui.setting.SettingViewModel
import com.example.homeactivity.ui.setting.SettingViewModelFactory
import com.example.homeactivity.ui.setting.dataStore
import com.google.android.material.floatingactionbutton.FloatingActionButton

class UpcomingDetailActivity : AppCompatActivity() {

    private lateinit var viewModel: FavoriteEventAddUpdateViewModel
    private var isFavorite: Boolean = false // Variabel untuk melacak status favorit
    private lateinit var binding: ActivityMainBinding
    private lateinit var settingViewModel: SettingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upcoming_detail)

        val pref = SettingPreferences.getInstance(application.dataStore)
        val factory = SettingViewModelFactory(pref)
        settingViewModel = ViewModelProvider(this, factory).get(SettingViewModel::class.java)

        // Ambil data event yang dikirim dari activity sebelumnya
        val eventItem: ListEventsItem? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("EVENT_DATA", ListEventsItem::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("EVENT_DATA")
        }

        // Binding layout
        val eventImage = findViewById<ImageView>(R.id.img_event_logo)
        val eventName = findViewById<TextView>(R.id.tv_event_name)
        val eventOwner = findViewById<TextView>(R.id.tv_owner_name)
        val eventTime = findViewById<TextView>(R.id.tv_event_time)
        val eventQuota = findViewById<TextView>(R.id.tv_event_quota)
        val eventDescription = findViewById<TextView>(R.id.tv_event_description)
        val btnRegist = findViewById<Button>(R.id.btn_regist)

        // Observe status tema dari ViewModel
        settingViewModel.getThemeSettings().observe(this) { isDarkModeActive ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                // Set warna putih untuk mode gelap
                eventName.setTextColor(ContextCompat.getColor(this, R.color.white))
                eventOwner.setTextColor(ContextCompat.getColor(this, R.color.white))
                eventTime.setTextColor(ContextCompat.getColor(this, R.color.white))
                eventQuota.setTextColor(ContextCompat.getColor(this, R.color.white))
                eventDescription.setTextColor(ContextCompat.getColor(this, R.color.white))
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        // Jika eventItem tidak null, tampilkan datanya
        eventItem?.let {
            eventName.text = it.name
            eventOwner.text = it.ownerName
            eventTime.text = it.beginTime
            val remainingQuota = it.quota?.minus(it.registrants ?: 0)
            eventQuota.text = "Sisa Kuota $remainingQuota"
            eventDescription.text = HtmlCompat.fromHtml(
                it.description ?: "No Description Available",
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )

            // Button Register
            btnRegist.setOnClickListener {
                val url = eventItem?.link // Mengakses properti link dari eventItem
                if (!url.isNullOrEmpty()) {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    startActivity(intent)
                } else {
                    // Jika link tidak ada, tampilkan pesan atau handling lain
                    btnRegist.text = "Link Not Available"
                }
            }

            // Tampilkan gambar menggunakan Glide
            Glide.with(this)
                .load(it.mediaCover)
                .placeholder(R.drawable.ic_launcher_background)
                .into(eventImage)

            // Inisialisasi ViewModel
            viewModel = ViewModelProvider(this).get(FavoriteEventAddUpdateViewModel::class.java)

            // Inisialisasi FloatingActionButton
            val fabFavorite: FloatingActionButton = findViewById(R.id.fab_favorite)
            fabFavorite.setOnClickListener {
                eventItem?.let { event ->
                    if (isFavorite) {
                        // Jika sudah favorit, hapus dari favorit
                        deleteFromFavorites(event)
                        fabFavorite.setImageResource(R.drawable.favorite_border) // Ganti icon ke tidak favorit
                        isFavorite = false // Update status favorit
                    } else {
                        // Jika belum favorit, tambahkan ke favorit
                        saveToFavorites(event)
                        fabFavorite.setImageResource(R.drawable.favorite) // Ganti icon ke favorit
                        isFavorite = true // Update status favorit
                    }
                }
            }
        } ?: run {
            // Jika eventItem null, tampilkan handling error
            eventName.text = "Event Not Found"
        }
    }

    private fun saveToFavorites(eventItem: ListEventsItem) {
        val favoriteEvent = FavoriteEvent(
            id = eventItem.id?.toString() ?: "",
            name = eventItem.name ?: "Unknown Event",
            ownerName = eventItem.ownerName ?: "Unknown Owner",
            quota = eventItem.quota ?: 0,
            registrant = eventItem.registrants ?: 0,
            beginTime = eventItem.beginTime ?: "Unknown Time",
            description = eventItem.description ?: "No Description Available",
            mediaCover = eventItem.mediaCover,
            link = eventItem.link ?: ""
        )
        viewModel.insert(favoriteEvent)
        Toast.makeText(this, "Event telah ditambahkan ke favorit", Toast.LENGTH_SHORT).show()
    }

    private fun deleteFromFavorites(eventItem: ListEventsItem) {
        // Logika untuk menghapus event dari favorit
        viewModel.delete(
            FavoriteEvent(
            id = eventItem.id?.toString() ?: "",
            name = eventItem.name ?: "Unknown Event",
            ownerName = eventItem.ownerName ?: "Unknown Owner",
            quota = eventItem.quota ?: 0,
            registrant = eventItem.registrants ?: 0,
            beginTime = eventItem.beginTime ?: "Unknown Time",
            description = eventItem.description ?: "No Description Available",
            mediaCover = eventItem.mediaCover,
            link = eventItem.link ?: ""
        )
        )
        Toast.makeText(this, "Event telah dihapus dari favorit", Toast.LENGTH_SHORT).show()
    }
}



