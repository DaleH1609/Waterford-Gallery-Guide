package org.wit.waterfordgalleryguide.activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.zxing.client.android.Intents.Scan.RESULT
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import org.wit.waterfordgalleryguide.R
import org.wit.waterfordgalleryguide.adapter.GalleryAdapter
import org.wit.waterfordgalleryguide.adapter.GalleryListener
import org.wit.waterfordgalleryguide.databinding.ActivityGalleryListBinding
import org.wit.waterfordgalleryguide.main.MainApp
import org.wit.waterfordgalleryguide.models.GalleryModel


class GalleryListActivity : AppCompatActivity(), GalleryListener {

    lateinit var app: MainApp
    private lateinit var binding: ActivityGalleryListBinding
    private lateinit var refreshIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var mapsIntentLauncher : ActivityResultLauncher<Intent>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGalleryListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)

        app = application as MainApp

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        loadGalleries()

        registerRefreshCallback()
        registerMapCallback()

        binding.scanQR.setOnClickListener() {
            val scanner = IntentIntegrator(this)
            scanner.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
            scanner.setBeepEnabled(false)
            scanner.initiateScan()
        }
}

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)

        if (result != null && result.contents != null) {
            val qrResult = result.contents
            if (qrResult.contains("https://") || qrResult.contains("http://")) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(qrResult))
                startActivity(intent)
            } else {
                binding.scanQR.text = qrResult
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_add -> {
                val launcherIntent = Intent(this, GalleryActivity::class.java)
                refreshIntentLauncher.launch(launcherIntent)
            }
            R.id.item_map -> {
                val launcherIntent = Intent(this, GalleryMapsActivity::class.java)
                mapsIntentLauncher.launch(launcherIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onGalleryClick(gallery: GalleryModel) {
        val launcherIntent = Intent(this, GalleryActivity::class.java)
        launcherIntent.putExtra("gallery_edit", gallery)
        refreshIntentLauncher.launch(launcherIntent)
    }


    private fun registerRefreshCallback() {
        refreshIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { loadGalleries() }
    }

    private fun loadGalleries() {
        showGalleries(app.galleries.findAll())
    }

    fun showGalleries (galleries: List<GalleryModel>) {
        binding.recyclerView.adapter = GalleryAdapter(galleries, this)
        binding.recyclerView.adapter?.notifyDataSetChanged()
    }

    private fun registerMapCallback() {
        mapsIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { }
    }
}
