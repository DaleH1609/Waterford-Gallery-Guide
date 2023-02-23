package org.wit.waterfordgalleryguide.models

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GalleryModel(var id: Long = 0,
                        var image: Uri = Uri.EMPTY,
                        var title: String = "",
                        var description: String = "",
                        var lat : Double = 0.0,
                        var lng: Double = 0.0,
                        var zoom: Float = 0f) : Parcelable

@Parcelize
data class Location(var lat: Double = 0.0,
                    var lng: Double = 0.0,
                    var zoom: Float = 0f) : Parcelable
