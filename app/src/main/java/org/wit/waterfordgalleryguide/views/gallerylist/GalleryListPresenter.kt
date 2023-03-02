package org.wit.waterfordgalleryguide.views.gallerylist

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import org.wit.waterfordgalleryguide.main.MainApp
import org.wit.waterfordgalleryguide.models.GalleryModel
import org.wit.waterfordgalleryguide.views.gallery.GalleryView
import org.wit.waterfordgalleryguide.views.gallerylist.GalleryListView
import org.wit.waterfordgalleryguide.views.map.GalleryMapView

class GalleryListPresenter(val view: GalleryListView) {

    var app: MainApp = view.application as MainApp
    private lateinit var refreshIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var editIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>

    init {
        registerEditCallback()
        registerRefreshCallback()
    }

    fun getGalleries() = app.galleries.findAll()

    fun doAddGallery() {
        val launcherIntent = Intent(view, GalleryView::class.java)
        editIntentLauncher.launch(launcherIntent)
    }

    fun doEditGallery(gallery: GalleryModel) {
        val launcherIntent = Intent(view, GalleryView::class.java)
        launcherIntent.putExtra("gallery_edit", gallery)
        editIntentLauncher.launch(launcherIntent)
    }

    fun doShowGalleriesMap() {
        val launcherIntent = Intent(view, GalleryMapView::class.java)
        editIntentLauncher.launch(launcherIntent)
    }
    private fun registerRefreshCallback() {
        refreshIntentLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { getGalleries() }
    }
    private fun registerEditCallback() {
        editIntentLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            {  }

    }
}