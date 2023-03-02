package org.wit.waterfordgalleryguide.views.map

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.squareup.picasso.Picasso
import org.wit.waterfordgalleryguide.databinding.ActivityGalleryMapsBinding
import org.wit.waterfordgalleryguide.databinding.ContentGalleryMapsBinding
import org.wit.waterfordgalleryguide.main.MainApp
import org.wit.waterfordgalleryguide.models.GalleryModel

class GalleryMapView : AppCompatActivity() , GoogleMap.OnMarkerClickListener{

    private lateinit var binding: ActivityGalleryMapsBinding
    private lateinit var contentBinding: ContentGalleryMapsBinding
    lateinit var app: MainApp
    lateinit var presenter: GalleryMapPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = application as MainApp
        binding = ActivityGalleryMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        presenter = GalleryMapPresenter(this)

        contentBinding = ContentGalleryMapsBinding.bind(binding.root)

        contentBinding.mapView.onCreate(savedInstanceState)
        contentBinding.mapView.getMapAsync{
            presenter.doPopulateMap(it)
        }
    }
    fun showGallery(gallery: GalleryModel) {
        contentBinding.currentTitle.text = gallery.title
        contentBinding.currentDescription.text = gallery.description
        Picasso.get()
            .load(gallery.image)
            .into(contentBinding.imageView2)
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        presenter.doMarkerSelected(marker)
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        contentBinding.mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        contentBinding.mapView.onLowMemory()
    }

    override fun onPause() {
        super.onPause()
        contentBinding.mapView.onPause()
    }

    override fun onResume() {
        super.onResume()
        contentBinding.mapView.onResume()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        contentBinding.mapView.onSaveInstanceState(outState)
    }


}