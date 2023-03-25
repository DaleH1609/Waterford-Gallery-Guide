package org.wit.waterfordgalleryguide.models

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AllGalleriesModel(var id: Long = 0,
                             var allTitle: String = "",
                             var allDescription: String = "",
                             var lat: Double = 0.0,
                             var lng: Double = 0.0,
                             var zoom: Float = 0f,
                             var newimage: Uri = Uri.EMPTY) : Parcelable

@Parcelize
data class AllLocation(var lat: Double = 0.0,
                    var lng: Double = 0.0,
                    var zoom: Float = 0f) : Parcelable

