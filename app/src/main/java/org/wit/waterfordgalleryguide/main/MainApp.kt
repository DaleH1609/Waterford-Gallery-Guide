package org.wit.waterfordgalleryguide.main

import android.app.Application
import org.wit.waterfordgalleryguide.models.*
import timber.log.Timber
import timber.log.Timber.i

class MainApp : Application() {

    lateinit var galleries: GalleryStore
    lateinit var allGalleries: AllGalleriesStore

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        allGalleries = AllGalleriesFireStore(applicationContext)
        galleries = GalleryFireStore(applicationContext)
        i("Gallery started")
    }
}