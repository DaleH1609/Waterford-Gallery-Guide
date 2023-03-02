package org.wit.waterfordgalleryguide.views.gallery

import android.annotation.SuppressLint
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import org.wit.waterfordgalleryguide.helpers.checkLocationPermissions
import org.wit.waterfordgalleryguide.helpers.showImagePicker
import org.wit.waterfordgalleryguide.main.MainApp
import org.wit.waterfordgalleryguide.models.GalleryModel
import org.wit.waterfordgalleryguide.models.Location
import org.wit.waterfordgalleryguide.views.location.EditLocationView
import timber.log.Timber

class GalleryPresenter(private val view: GalleryView) {
    var map: GoogleMap? = null
    var gallery = GalleryModel()
    var app: MainApp = view.application as MainApp
    var locationService: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(view)
    private lateinit var imageIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    var edit = false;
    private val location = Location(52.245696, -7.139102, 15f)

    init {

        doPermissionLauncher()
        registerImagePickerCallback()
        registerMapCallback()

        if (view.intent.hasExtra("gallery_edit")) {
            edit = true
            gallery = view.intent.extras?.getParcelable("gallery_edit")!!
            view.showGallery(gallery)
        }
        else {

            if (checkLocationPermissions(view)) {
                doSetCurrentLocation()
            }
            gallery.lat = location.lat
            gallery.lng = location.lng
        }

    }


    fun doAddOrSave(title: String, description: String) {
        gallery.title = title
        gallery.description = description
        if (edit) {
            app.galleries.update(gallery)
        } else {
            app.galleries.create(gallery)
        }

        view.finish()

    }

    fun doCancel() {
        view.finish()

    }

    fun doDelete() {
        app.galleries.delete(gallery)
        view.finish()

    }

    fun doSelectImage() {
        showImagePicker(imageIntentLauncher)
    }

    fun doSetLocation() {

        if (gallery.zoom != 0f) {
            location.lat =  gallery.lat
            location.lng = gallery.lng
            location.zoom = gallery.zoom
            locationUpdate(gallery.lat, gallery.lng)
        }
        val launcherIntent = Intent(view, EditLocationView::class.java)
            .putExtra("location", location)
        mapIntentLauncher.launch(launcherIntent)
    }

    @SuppressLint("MissingPermission")
    fun doSetCurrentLocation() {
        Timber.i("setting location from doSetLocation")
        locationService.lastLocation.addOnSuccessListener {
            locationUpdate(it.latitude, it.longitude)
        }
    }

    fun cacheGallery (title: String, description: String) {
        gallery.title = title;
        gallery.description = description
    }
    fun doConfigureMap(m: GoogleMap) {
        map = m
        locationUpdate(gallery.lat, gallery.lng)
    }

    fun locationUpdate(lat: Double, lng: Double) {
        gallery.lat = lat
        gallery.lng = lng
        gallery.zoom = 15f
        map?.clear()
        map?.uiSettings?.setZoomControlsEnabled(true)
        val options = MarkerOptions().title(gallery.title).position(LatLng(gallery.lat, gallery.lng))
        map?.addMarker(options)
        map?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(gallery.lat, gallery.lng), gallery.zoom))
        view.showGallery(gallery)
    }



    private fun registerImagePickerCallback() {

        imageIntentLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when(result.resultCode){
                    AppCompatActivity.RESULT_OK -> {
                        if (result.data != null) {
                            Timber.i("Got Result ${result.data!!.data}")
                            gallery.image = result.data!!.data!!
                            view.updateImage(gallery.image)
                        }
                    }
                    AppCompatActivity.RESULT_CANCELED -> { } else -> { }
                }

            }
    }

    private fun registerMapCallback() {
        mapIntentLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when (result.resultCode) {
                    AppCompatActivity.RESULT_OK -> {
                        if (result.data != null) {
                            Timber.i("Got Location ${result.data.toString()}")
                            val location = result.data!!.extras?.getParcelable<Location>("location")!!
                            Timber.i("Location == $location")
                            location.lat = location.lat
                            location.lng = location.lng
                            location.zoom = location.zoom
                        } // end of if
                    }
                    AppCompatActivity.RESULT_CANCELED -> { } else -> { }
                }

            }
    }
    private fun doPermissionLauncher() {
        Timber.i("permission check called")
        requestPermissionLauncher =
            view.registerForActivityResult(ActivityResultContracts.RequestPermission())
            { isGranted: Boolean ->
                if (isGranted) {
                    doSetCurrentLocation()
                } else {
                    locationUpdate(location.lat, location.lng)
                }
            }
    }
}