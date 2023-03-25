package org.wit.waterfordgalleryguide.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.squareup.picasso.Picasso
import org.wit.waterfordgalleryguide.R
import org.wit.waterfordgalleryguide.databinding.ActivityAllGalleriesBinding
import org.wit.waterfordgalleryguide.models.AllGalleriesModel
import org.wit.waterfordgalleryguide.models.AllLocation
import timber.log.Timber
import timber.log.Timber.i

class AllGalleriesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAllGalleriesBinding
    var gallery = AllGalleriesModel()
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var imageIntentLauncher : ActivityResultLauncher<Intent>
    val IMAGE_REQUEST = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAllGalleriesBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_all_galleries)
        setSupportActionBar(binding.toolbarAllAdd)



        Timber.plant(Timber.DebugTree())

           if (intent.hasExtra("galleries_edit")) {
               gallery = intent.extras?.getParcelable("galleries_edit")!!
               binding.allGalleryTitle.setText(gallery.allTitle)
               binding.allGalleryDescription.setText(gallery.allDescription)
             }

        gallery.allTitle = binding.allGalleryTitle.text.toString()
        gallery.allDescription = binding.allGalleryDescription.text.toString()
        Picasso.get()
            .load(gallery.newimage)
            .into(binding.allGalleryImage)

        binding.allGalleriesLocation.setOnClickListener {
            val location = AllLocation(52.245696, -7.139102, 15f)
            val launcherIntent = Intent(this, MapActivity::class.java)
                .putExtra("location", location)
            mapIntentLauncher.launch(launcherIntent)
        }



        registerImagePickerCallback()
        registerMapCallback()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_all_galleries, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun registerMapCallback() {
        mapIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when (result.resultCode) {
                    RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Location ${result.data.toString()}")
                            val location = result.data!!.extras?.getParcelable<AllLocation>("location")!!
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_cancel -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun registerImagePickerCallback() {
        imageIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when(result.resultCode){
                    RESULT_OK -> {
                        if (result.data != null) {
                            Timber.i("Got Result ${result.data!!.data}")
                            gallery.newimage = result.data!!.data!!
                            Picasso.get()
                                .load(gallery.newimage)
                                .into(binding.allGalleryImage)
                        } // end of if
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
    }
}

