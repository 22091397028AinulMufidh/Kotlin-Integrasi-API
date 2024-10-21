package com.example.homeactivity.ui.upcoming

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import com.bumptech.glide.Glide
import com.example.homeactivity.R
import com.example.homeactivity.data.response.ListEventsItem
import com.example.homeactivity.databinding.ActivityUpcomingDetailBinding

class UpcomingDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpcomingDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inisialisasi binding
        binding = ActivityUpcomingDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val eventItem: ListEventsItem? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("EVENT_DATA", ListEventsItem::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("EVENT_DATA")
        }

        // Jika eventItem tidak null, tampilkan datanya
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

            // Tampilkan gambar menggunakan Glide
            Glide.with(this)
                .load(it.mediaCover)
                .placeholder(R.drawable.ic_launcher_background)
                .into(binding.imgEventLogo)
        } ?: run {
            // Jika eventItem null, tampilkan handling error
            binding.tvEventName.text = "Event Not Found"
        }
    }
}
