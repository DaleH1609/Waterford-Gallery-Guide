package org.wit.waterfordgalleryguide.activities

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import org.wit.waterfordgalleryguide.R
import org.wit.waterfordgalleryguide.databinding.ContentAllGalleriesMapsBinding

class AllGalleriesMapsActivity : AppCompatActivity() {

    private lateinit var binding: AllGalleriesMapsActivity
    private lateinit var contentBinding: ContentAllGalleriesMapsBinding
    lateinit var map: GoogleMap

    fun onMarkerClick(marker: Marker): Boolean {
        val currentTitle: TextView = findViewById(R.id.currentTitle)
        currentTitle.text = marker.title

        return false
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