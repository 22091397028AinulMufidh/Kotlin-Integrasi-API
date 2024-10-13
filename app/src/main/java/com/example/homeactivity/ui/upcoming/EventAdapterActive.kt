package com.example.homeactivity.ui.upcoming

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.homeactivity.R
import com.example.homeactivity.data.response.ListEventsItemActive

class EventAdapterActive(private var eventList: List<ListEventsItemActive?>) :
    RecyclerView.Adapter<EventAdapterActive.EventViewHolder>() {

    inner class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.img_item_photo)
        val itemName: TextView = itemView.findViewById(R.id.item_name)
        val progressBar: ProgressBar = itemView.findViewById(R.id.progressBar)
        val endedTime: TextView = itemView.findViewById(R.id.end_time)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_finished, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val eventItem = eventList[position]

        // Set event name
        holder.itemName.text = eventItem?.name ?: "No Name Available"

        holder.endedTime.text =eventItem?.endTime ?: "No Time Available"

        // Load image using Glide or other image loading libraries
        if (eventItem?.imageLogo != null) {
            holder.progressBar.visibility = View.VISIBLE
            Glide.with(holder.itemView.context)
                .load(eventItem.imageLogo)
                .into(holder.imageView)
            holder.progressBar.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return eventList.size
    }

    // Update data method for updating list
    fun updateData(newEventList: List<ListEventsItemActive?>) {
        eventList = newEventList
        notifyDataSetChanged() // Notify the adapter to update UI
    }
}
