package org.wit.waterfordgalleryguide.views.gallerylist

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.zxing.integration.android.IntentIntegrator
import org.wit.waterfordgalleryguide.R
import org.wit.waterfordgalleryguide.databinding.ActivityGalleryListBinding
import org.wit.waterfordgalleryguide.main.MainApp
import org.wit.waterfordgalleryguide.models.GalleryModel
import timber.log.Timber

class GalleryListView : AppCompatActivity(), GalleryListener {

    lateinit var app: MainApp
    lateinit var binding: ActivityGalleryListBinding
    lateinit var presenter: GalleryListPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.i("Recycler View Loaded")
        super.onCreate(savedInstanceState)
        binding = ActivityGalleryListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)
        presenter = GalleryListPresenter(this)
        //app = application as MainApp

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter =
            GalleryAdapter(presenter.getGalleries(), this)

        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            binding.toolbar.title = "${title}: ${user.email}"
        }

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

    override fun onResume() {
        //update the view
        binding.recyclerView.adapter?.notifyDataSetChanged()
        Timber.i("recyclerView onResume")
        super.onResume()
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_add -> { presenter.doAddGallery() }
            R.id.item_map -> { presenter.doShowGalleriesMap() }
            R.id.item_logout -> { presenter.doLogout() }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onGalleryClick(gallery: GalleryModel) {
        presenter.doEditGallery(gallery)

    }

}