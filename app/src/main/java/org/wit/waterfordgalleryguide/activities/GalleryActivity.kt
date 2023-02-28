package org.wit.waterfordgalleryguide.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import org.wit.waterfordgalleryguide.R
import org.wit.waterfordgalleryguide.databinding.ActivityGalleryBinding
import org.wit.waterfordgalleryguide.helpers.showImagePicker
import org.wit.waterfordgalleryguide.main.MainApp
import org.wit.waterfordgalleryguide.models.GalleryModel
import org.wit.waterfordgalleryguide.models.Location
import timber.log.Timber.i

class GalleryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGalleryBinding
    private lateinit var imageIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>
    var gallery = GalleryModel()
    lateinit var app: MainApp
    var edit = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerImagePickerCallback()
        binding = ActivityGalleryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)
        app = application as MainApp

        if (intent.hasExtra("gallery_edit")) {
            edit = true
            gallery = intent.extras?.getParcelable("gallery_edit")!!
            binding.galleryTitle.setText(gallery.title)
            binding.description.setText(gallery.description)
            binding.btnAdd.setText(R.string.save_placemark)
            Picasso.get()
                .load(gallery.image)
                .into(binding.galleryImage)
            if (gallery.image != Uri.EMPTY) {
                binding.chooseImage.setText(R.string.change_placemark_image)
            }
        }

        binding.btnAdd.setOnClickListener() {
            gallery.title = binding.galleryTitle.text.toString()
            gallery.description = binding.description.text.toString()
            if (gallery.title.isEmpty()) {
                Snackbar.make(it, R.string.enter_placemark_title, Snackbar.LENGTH_LONG)
                    .show()
            } else {
                if (edit) {
                    app.galleries.update(gallery.copy())
                } else {
                    app.galleries.create(gallery.copy())
                }
            }
            setResult(RESULT_OK)
            finish()
        }

        binding.chooseImage.setOnClickListener {
            showImagePicker(imageIntentLauncher)
        }

        binding.galleryLocation.setOnClickListener {
            val location = Location(52.245696, -7.139102, 15f)
            if (gallery.zoom != 0f) {
                location.lat =  gallery.lat
                location.lng = gallery.lng
                location.zoom = gallery.zoom
            }
            val launcherIntent = Intent(this, MapActivity::class.java)
                .putExtra("location", location)
            mapIntentLauncher.launch(launcherIntent)
        }

        registerImagePickerCallback()
        registerMapCallback()
    }

    private fun registerMapCallback() {
        mapIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when (result.resultCode) {
                    RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Location ${result.data.toString()}")
                            val location = result.data!!.extras?.getParcelable<Location>("location")!!
                            i("Location == $location")
                            gallery.lat = location.lat
                            gallery.lng = location.lng
                            gallery.zoom = location.zoom
                        } // end of if
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
    }

    private fun registerImagePickerCallback() {
        imageIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when(result.resultCode){
                    RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Result ${result.data!!.data}")
                            gallery.image = result.data!!.data!!
                            Picasso.get()
                                .load(gallery.image)
                                .into(binding.galleryImage)
                            binding.chooseImage.setText(R.string.change_placemark_image)
                        } // end of if
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_gallery, menu)
        if (edit && menu != null) menu.getItem(0).setVisible(true)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_delete -> {
                app.galleries.delete(gallery)
                finish()
            }
            R.id.item_cancel -> { finish() }
        }
        return super.onOptionsItemSelected(item)
    }
}
