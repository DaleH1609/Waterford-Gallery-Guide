package org.wit.waterfordgalleryguide.models

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class GalleryFireStore(val context: Context) : GalleryStore {
    val galleries = ArrayList<GalleryModel>()
    lateinit var userId: String
    lateinit var db: DatabaseReference

    override fun findAll(): List<GalleryModel> {
        return galleries
    }

    override fun findById(id: Long): GalleryModel? {
        val foundPlacemark: GalleryModel? = galleries.find { p -> p.id == id }
        return foundPlacemark
    }

    override fun create(gallery: GalleryModel) {
        val key = db.child("users").child(userId).child("galleries").push().key
        key?.let {
            gallery.fbId = key
            galleries.add(gallery)
            db.child("users").child(userId).child("galleries").child(key).setValue(gallery)
        }
    }

    override fun update(gallery: GalleryModel) {
        var foundGallery: GalleryModel? = galleries.find { p -> p.fbId == gallery.fbId }
        if (foundGallery != null) {
            foundGallery.title = gallery.title
            foundGallery.description = gallery.description
            foundGallery.image = gallery.image
            foundGallery.location = gallery.location
        }

        db.child("users").child(userId).child("galleries").child(gallery.fbId).setValue(gallery)

    }

    override fun delete(gallery: GalleryModel) {
        db.child("users").child(userId).child("placemarks").child(gallery.fbId).removeValue()
        galleries.remove(gallery)
    }

  //  override fun clear() {
  //      galleries.clear()
  //  }

    fun fetchGalleries(galleriesReady: () -> Unit) {
        val valueEventListener = object : ValueEventListener {
            override fun onCancelled(dataSnapshot: DatabaseError) {
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot!!.children.mapNotNullTo(galleries) {
                    it.getValue<GalleryModel>(
                        GalleryModel::class.java
                    )
                }
                galleriesReady()
            }
        }
        userId = FirebaseAuth.getInstance().currentUser!!.uid
        db = FirebaseDatabase.getInstance("https://waterford-gallery-guide-404b6-default-rtdb.firebaseio.com/").reference
       // galleries.clear()
        db.child("users").child(userId).child("galleries")
            .addListenerForSingleValueEvent(valueEventListener)
    }
}