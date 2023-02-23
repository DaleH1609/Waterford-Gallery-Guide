package org.wit.waterfordgalleryguide.main

import android.app.Application
import org.wit.waterfordgalleryguide.models.GalleryMemStore
import org.wit.waterfordgalleryguide.models.GalleryModel
import org.wit.waterfordgalleryguide.models.GalleryStore
import timber.log.Timber
import timber.log.Timber.i

class MainApp : Application() {

   //  val galleries = ArrayList<GalleryModel>()

   // val galleries = GalleryMemStore()

    lateinit var galleries: GalleryStore

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        galleries = GalleryMemStore()
        i("Gallery started")
    }
}