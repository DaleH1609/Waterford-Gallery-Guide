package org.wit.waterfordgalleryguide.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import org.wit.waterfordgalleryguide.R
import org.wit.waterfordgalleryguide.models.AllLocation

class MapActivity : AppCompatActivity(), OnMapReadyCallback,
    GoogleMap.OnMarkerDragListener,
    GoogleMap.OnMarkerClickListener {

    private lateinit var mMap: GoogleMap
    var location = AllLocation()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map2)
        location = intent.extras?.getParcelable<AllLocation>("location")!!
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val loc = LatLng(location.lat, location.lng)
        val options = MarkerOptions()
            .title("Gallery")
            .snippet("GPS : $loc")
            .position(loc)
        mMap.addMarker(options)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, location.zoom))
        mMap.setOnMarkerDragListener(this)
        mMap.setOnMarkerClickListener(this)
    }

    override fun onMarkerDragStart(marker: Marker) {
    }

    override fun onMarkerDrag(marker: Marker) {
    }

    override fun onMarkerDragEnd(marker: Marker) {
        location.lat = marker.position.latitude
        location.lng = marker.position.longitude
        location.zoom = mMap.cameraPosition.zoom
    }

    override fun onBackPressed() {
        val resultIntent = Intent()
        resultIntent.putExtra("location", location)
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
        super.onBackPressed()
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        val loc = LatLng(location.lat, location.lng)
        marker.snippet = "GPS : $loc"
        return false
    }
}