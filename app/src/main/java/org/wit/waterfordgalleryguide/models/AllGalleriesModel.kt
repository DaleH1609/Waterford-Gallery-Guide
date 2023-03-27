package org.wit.waterfordgalleryguide.models

import android.net.Uri
import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class AllGalleriesModel(@PrimaryKey(autoGenerate = true) var id: Long = 0,
                             var fbId: String = "",
                             var allTitle: String = "",
                             var allDescription: String = "",
                             var newimage: String = "",
                             var lat: Double = 0.0,
                             var lng: Double = 0.0,
                             var zoom: Float = 0f,
                             @Embedded var location : Location = Location()): Parcelable

@Parcelize
data class AllLocation(var lat: Double = 0.0,
                       var lng: Double = 0.0,
                       var zoom: Float = 0f) : Parcelable

