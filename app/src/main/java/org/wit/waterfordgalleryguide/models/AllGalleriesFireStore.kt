package org.wit.waterfordgalleryguide.models

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class AllGalleriesFireStore(val context: Context) : AllGalleriesStore{

    val allGallery = ArrayList<AllGalleriesModel>()
    lateinit var userId: String
    lateinit var db: DatabaseReference

    override fun findAll(): List<AllGalleriesModel> {
        return allGallery
    }

    override fun create(allGalleries: AllGalleriesModel) {
        val key = db.child("global").child("allGalleries").push().key
        key?.let {
            allGalleries.fbId = key
            allGallery.add(allGalleries)
            db.child("global").child("allGalleries").child(key).setValue(allGalleries)
        }
    }

    fun findById(id: Long): AllGalleriesModel? {
        val foundAllGalleries: AllGalleriesModel? = allGallery.find { p -> p.id == id }
        return foundAllGalleries
    }

    fun fetchAllGalleries(allGalleriesReady: () -> Unit) {
        val valueEventListener = object : ValueEventListener {
            override fun onCancelled(dataSnapshot: DatabaseError) {
            }
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot!!.children.mapNotNullTo(allGallery) {
                    it.getValue<AllGalleriesModel>(
                        AllGalleriesModel::class.java
                    )
                }
                allGalleriesReady()
            }
        }
        userId = FirebaseAuth.getInstance().currentUser!!.uid
        db = FirebaseDatabase.getInstance("https://waterford-gallery-guide-404b6-default-rtdb.firebaseio.com/").reference
        db.child("global").child("allGalleries")
            .addListenerForSingleValueEvent(valueEventListener)
    }
}
