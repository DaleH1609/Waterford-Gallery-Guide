package org.wit.waterfordgalleryguide.views.gallerylist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import org.wit.waterfordgalleryguide.databinding.CardGalleryBinding
import org.wit.waterfordgalleryguide.models.GalleryModel

interface GalleryListener {
    fun onGalleryClick(gallery: GalleryModel)
}

class GalleryAdapter constructor(private var galleries: List<GalleryModel>,
                                   private val listener: GalleryListener) :
    RecyclerView.Adapter<GalleryAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardGalleryBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val gallery = galleries[holder.adapterPosition]
        holder.bind(gallery, listener)
    }

    override fun getItemCount(): Int = galleries.size

    class MainHolder(private val binding : CardGalleryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(gallery: GalleryModel, listener: GalleryListener) {
            binding.galleryTitle.text = gallery.title
            binding.galleryDescription.text = gallery.description
            binding.ratingBar.rating = gallery.rating
            if (gallery.image != ""){
            Picasso.get()
                .load(gallery.image)
                .resize(200,200)
                .into(binding.imageIcon)
                }
            binding.root.setOnClickListener { listener.onGalleryClick(gallery) }
        }
    }
}
