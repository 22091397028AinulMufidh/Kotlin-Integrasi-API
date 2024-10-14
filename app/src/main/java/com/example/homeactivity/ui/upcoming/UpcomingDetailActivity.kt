package com.example.homeactivity.ui.upcoming

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
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
        val btnRegist = findViewById<Button>(R.id.btn_regist)

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
                val url = "https://www.dicoding.com/events/"

                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
            }

            // Tampilkan gambar menggunakan Glide
            Glide.with(this)
                .load(it.mediaCover)
                .placeholder(R.drawable.ic_launcher_background)
                .into(eventImage)
        } ?: run {
            // Jika eventItem null, tampilkan handling error
            eventName.text = "Event Not Found"
        }
    }
}