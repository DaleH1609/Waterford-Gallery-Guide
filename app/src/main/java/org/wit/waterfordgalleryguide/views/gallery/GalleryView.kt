package org.wit.waterfordgalleryguide.views.gallery

import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.GoogleMap
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import org.wit.waterfordgalleryguide.R
import org.wit.waterfordgalleryguide.databinding.ActivityGalleryBinding
import org.wit.waterfordgalleryguide.models.GalleryModel
import org.wit.waterfordgalleryguide.models.Location
import timber.log.Timber
import timber.log.Timber.i

class GalleryView : AppCompatActivity() {

    private lateinit var binding: ActivityGalleryBinding
    private lateinit var presenter: GalleryPresenter
    lateinit var map: GoogleMap
    var gallery = GalleryModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGalleryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)

        presenter = GalleryPresenter(this)

        binding.chooseImage.setOnClickListener {
            presenter.cacheGallery(binding.galleryTitle.text.toString(), binding.description.text.toString())
            presenter.doSelectImage()
        }

        binding.mapView2.setOnClickListener {
            presenter.cacheGallery(binding.galleryTitle.text.toString(), binding.description.text.toString())
            presenter.doSetLocation()
        }

        binding.mapView2.onCreate(savedInstanceState);
        binding.mapView2.getMapAsync {
            map = it
            presenter.doConfigureMap(map)
            it.setOnMapClickListener { presenter.doSetLocation() }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_gallery, menu)
        val deleteMenu: MenuItem = menu.findItem(R.id.item_delete)
        if (presenter.edit){
            deleteMenu.setVisible(true)
        }
        else{
            deleteMenu.setVisible(false)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_save -> {
                if (binding.galleryTitle.text.toString().isEmpty()) {
                    Snackbar.make(binding.root, R.string.enter_placemark_title, Snackbar.LENGTH_LONG)
                        .show()
                } else {
                    presenter.doAddOrSave(binding.galleryTitle.text.toString(), binding.description.text.toString())
                }
            }
            R.id.item_delete -> {
                presenter.doDelete()
            }
            R.id.item_cancel -> {
                presenter.doCancel()
            }

        }
        return super.onOptionsItemSelected(item)
    }

    fun showGallery(gallery: GalleryModel) {
        if (binding.galleryTitle.text.isEmpty()) binding.galleryTitle.setText(gallery.title)
        if (binding.description.text.isEmpty())  binding.description.setText(gallery.description)

        if (gallery.image != "") {
            Picasso.get()
                .load(gallery.image)
                .into(binding.galleryImage)

            binding.chooseImage.setText(R.string.change_placemark_image)
        }
        this.showLocation(gallery.location)
    }

   private fun showLocation (loc : Location) {
        binding.lat.setText("%.6f".format(loc.lat))
        binding.lng.setText("%.6f".format(loc.lng))
    }

    fun updateImage(image: String){
        i("Image updated")
        Picasso.get()
            .load(image)
            .into(binding.galleryImage)
        binding.chooseImage.setText(R.string.change_placemark_image)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mapView2.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView2.onLowMemory()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView2.onPause()
    }

    override fun onResume() {
        super.onResume()
        binding.mapView2.onResume()
        presenter.doRestartLocationUpdates()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapView2.onSaveInstanceState(outState)
    }

}