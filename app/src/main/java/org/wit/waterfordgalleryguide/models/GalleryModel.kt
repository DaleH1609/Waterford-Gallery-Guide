package org.wit.waterfordgalleryguide.models

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Parcelize
@Entity
data class GalleryModel(@PrimaryKey(autoGenerate = true) var id: Long = 0,
                        var fbId: String = "",
                        var image: String = "",
                        var title: String = "",
                        var description: String = "",
                        @Embedded var location : Location = Location()): Parcelable

@Parcelize
data class Location(var lat: Double = 0.0,
                    var lng: Double = 0.0,
                    var zoom: Float = 0f) : Parcelable

