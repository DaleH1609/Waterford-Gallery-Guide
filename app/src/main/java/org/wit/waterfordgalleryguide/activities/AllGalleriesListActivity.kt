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
import org.wit.waterfordgalleryguide.adapter.AllGalleriesAdapter
import org.wit.waterfordgalleryguide.adapter.AllGalleriesListener
import org.wit.waterfordgalleryguide.databinding.ActivityAllGalleriesListBinding
import org.wit.waterfordgalleryguide.databinding.ActivityButtonBinding.bind
import org.wit.waterfordgalleryguide.databinding.ActivityButtonBinding.inflate
import org.wit.waterfordgalleryguide.main.MainApp
import org.wit.waterfordgalleryguide.models.AllGalleriesModel
import org.wit.waterfordgalleryguide.models.GalleryModel
import org.wit.waterfordgalleryguide.views.login.LoginView

class AllGalleriesListActivity : AppCompatActivity(), AllGalleriesListener {

    lateinit var app: MainApp
    private lateinit var binding: ActivityAllGalleriesListBinding
    private lateinit var refreshIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var mapsIntentLauncher : ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAllGalleriesListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarAll)
        binding.toolbarAll.title = title

        app = application as MainApp
        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView1.layoutManager = layoutManager

        loadAllGalleries()
        registerRefreshCallback()
        registerMapCallback()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main_list_all, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_logout -> {
                val intent = Intent(this, LoginView::class.java)
                startActivity(intent)
            }
            R.id.item_map -> {
                val launcherIntent = Intent(this, AllGalleriesMapsActivity::class.java)
                refreshIntentLauncher.launch(launcherIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun loadAllGalleries() {
        showAllGalleries(app.allGalleries.findAll())
    }

    fun showAllGalleries (allGalleries: List<AllGalleriesModel>) {
        binding.recyclerView1.adapter = AllGalleriesAdapter(allGalleries, this)
        binding.recyclerView1.adapter?.notifyDataSetChanged()
    }

    private fun registerMapCallback() {
        mapsIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { }
    }

    private fun registerRefreshCallback() {
        refreshIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            {loadAllGalleries()}
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        binding.recyclerView1.adapter?.notifyDataSetChanged()
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onAllGalleriesClick(allGalleries: AllGalleriesModel) {
        val launcherIntent = Intent(this, AllGalleriesActivity::class.java)
        launcherIntent.putExtra("galleries_edit", allGalleries)
        refreshIntentLauncher.launch(launcherIntent)
    }
}