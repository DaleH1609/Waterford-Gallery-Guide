package org.wit.waterfordgalleryguide.models

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class AllGalleriesFireStore(val context: Context) : AllGalleriesStore{

    val allGallery = ArrayList<AllGalleriesModel>()
    lateinit var userId: String
    lateinit var db: DatabaseReference
    lateinit var st: StorageReference

    override fun findAll(): List<AllGalleriesModel> {
        return allGallery
    }

    override fun create(allGalleries: AllGalleriesModel) {
        val key = db.child("users").child(userId).child("allGalleries").push().key
        key?.let {
            allGalleries.fbId = key
            allGallery.add(allGalleries)
            db.child("global").child(userId).child("allGalleries").child(key).setValue(allGalleries)
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
        st = FirebaseStorage.getInstance().reference
        allGallery.clear()
        db.child("allGalleries")
            .addListenerForSingleValueEvent(valueEventListener)
    }
}
