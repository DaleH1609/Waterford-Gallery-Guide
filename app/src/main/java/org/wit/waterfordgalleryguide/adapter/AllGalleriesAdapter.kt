package org.wit.waterfordgalleryguide.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import org.wit.waterfordgalleryguide.databinding.CardAllGalleriesBinding
import org.wit.waterfordgalleryguide.models.AllGalleriesModel

interface AllGalleriesListener {
    fun onAllGalleriesClick(gallery: AllGalleriesModel)
}

class AllGalleriesAdapter constructor(
    private var allGalleries: List<AllGalleriesModel>,
    private val listener: AllGalleriesListener
    ) :
        RecyclerView.Adapter<AllGalleriesAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardAllGalleriesBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val gallery = allGalleries[holder.adapterPosition]
        holder.bind(gallery, listener)
    }

    override fun getItemCount(): Int = allGalleries.size

    class MainHolder(private val binding : CardAllGalleriesBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(allGalleries: AllGalleriesModel, listener: AllGalleriesListener) {
            binding.allGalleryTitle.text = allGalleries.allTitle
            binding.allGalleryDescription.text = allGalleries.allDescription
            if (allGalleries.newimage != "") {
                Picasso.get()
                    .load(allGalleries.newimage)
                    .resize(200, 200)
                    .into(binding.imageIcon2)
            }
            binding.root.setOnClickListener { listener.onAllGalleriesClick(allGalleries) }
        }
    }
}
