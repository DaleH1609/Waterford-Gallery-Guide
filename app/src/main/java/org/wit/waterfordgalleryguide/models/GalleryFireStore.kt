package org.wit.waterfordgalleryguide.models

import android.content.Context
import android.graphics.Bitmap
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import org.wit.waterfordgalleryguide.helpers.readImageFromPath
import timber.log.Timber
import java.io.ByteArrayOutputStream
import java.io.File

class GalleryFireStore(val context: Context) : GalleryStore {
    val galleries = ArrayList<GalleryModel>()
    lateinit var userId: String
    lateinit var db: DatabaseReference
    lateinit var st: StorageReference

    override fun findAll(): List<GalleryModel> {
        return galleries
    }

    override fun findById(id: Long): GalleryModel? {
        val foundGallery: GalleryModel? = galleries.find { p -> p.id == id }
        return foundGallery
    }

    override fun create(gallery: GalleryModel) {
        val key = db.child("users").child(userId).child("galleries").push().key
        key?.let {
            gallery.fbId = key
            galleries.add(gallery)
            db.child("users").child(userId).child("galleries").child(key).setValue(gallery)
            updateImage(gallery)
        }
    }

    fun updateImage(gallery: GalleryModel) {
        if (gallery.image != "") {
            val fileName = File(gallery.image)
            val imageName = fileName.getName()

            var imageRef = st.child(userId + '/' + imageName)
            val baos = ByteArrayOutputStream()
            val bitmap = readImageFromPath(context, gallery.image)

            bitmap?.let {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                val data = baos.toByteArray()
                val uploadTask = imageRef.putBytes(data)
                uploadTask.addOnFailureListener {
                    println(it.message)
                }.addOnSuccessListener { taskSnapshot ->
                    taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
                        gallery.image = it.toString()
                        db.child("users").child(userId).child("galleries").child(gallery.fbId).setValue(gallery)
                    }
                }.addOnFailureListener{
                var errorMessage = it.message
                Timber.i("Failure: $errorMessage")
              }
            }
        }
    }

    override fun update(gallery: GalleryModel) {
        var foundGallery: GalleryModel? = galleries.find { p -> p.fbId == gallery.fbId }
        if (foundGallery != null) {
            foundGallery.title = gallery.title
            foundGallery.description = gallery.description
            foundGallery.image = gallery.image
            foundGallery.rating = gallery.rating
            foundGallery.location = gallery.location
        }
        db.child("users").child(userId).child("galleries").child(gallery.fbId).setValue(gallery)
        if(gallery.image.length > 0){
            updateImage(gallery)
        }
    }

    override fun delete(gallery: GalleryModel) {
        db.child("users").child(userId).child("galleries").child(gallery.fbId).removeValue()
        galleries.remove(gallery)
    }

    override fun clear() {
        galleries.clear()
    }

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
        st = FirebaseStorage.getInstance().reference
        db = FirebaseDatabase.getInstance("https://waterford-gallery-guide-404b6-default-rtdb.firebaseio.com/").reference
        galleries.clear()
        db.child("users").child(userId).child("galleries")
            .addListenerForSingleValueEvent(valueEventListener)
    }
}