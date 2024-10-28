package com.example.homeactivity.data.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class FavoriteEvent(
    @PrimaryKey(autoGenerate = false)
    var id: String = "",
    var name: String = "",
    var ownerName: String = "",
    var quota: Int = 0,
    var registrant: Int = 0,
    var beginTime: String = "",
    var description: String = "",
    var mediaCover: String? = null,
    var link: String? = null
) : Parcelable


