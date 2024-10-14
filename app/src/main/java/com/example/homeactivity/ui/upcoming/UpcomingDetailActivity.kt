package com.example.homeactivity.ui.upcoming

import android.os.Build
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.homeactivity.R
import com.example.homeactivity.data.response.ListEventsItem

class UpcomingDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upcoming_detail)

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

        // Jika eventItem tidak null, tampilkan datanya
        eventItem?.let {
            eventName.text = it.name ?: "No Name"
            eventOwner.text = it.ownerName ?: "Unknown Organizer"
            eventTime.text = it.beginTime ?: "No Date"
            val remainingQuota = it.quota?.minus(it.registrants ?: 0) ?: 0
            eventQuota.text = "$remainingQuota Sisa Kuota"
            eventDescription.text = it.description ?: "No Description Available"

            // Tampilkan gambar menggunakan Glide atau library gambar lainnya
            Glide.with(this)
                .load(it.imageLogo ?: it.mediaCover) // Coba gunakan mediaCover jika imageLogo tidak ada
                .placeholder(R.drawable.ic_launcher_background) // Placeholder jika gambar tidak ada
                .into(eventImage)
        } ?: run {
            // Jika eventItem null, tampilkan pesan default atau handling error
            eventName.text = "Event Not Found"
        }
    }
}