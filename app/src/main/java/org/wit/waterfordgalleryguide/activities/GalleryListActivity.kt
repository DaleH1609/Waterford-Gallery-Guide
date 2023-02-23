package org.wit.waterfordgalleryguide.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGalleryListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)

        app = application as MainApp

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = GalleryAdapter(app.galleries.findAll(),this)

        registerRefreshCallback()
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
            { binding.recyclerView.adapter?.notifyDataSetChanged() }
    }
}
