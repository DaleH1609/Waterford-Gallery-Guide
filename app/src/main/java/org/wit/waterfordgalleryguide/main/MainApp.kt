package org.wit.waterfordgalleryguide.main

import android.app.Application
import org.wit.waterfordgalleryguide.models.GalleryFireStore
import org.wit.waterfordgalleryguide.models.GalleryJSONStore
import org.wit.waterfordgalleryguide.models.GalleryStore
import timber.log.Timber
import timber.log.Timber.i

class MainApp : Application() {

    lateinit var galleries: GalleryStore

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        galleries = GalleryFireStore(applicationContext)
        i("Gallery started")
    }
}