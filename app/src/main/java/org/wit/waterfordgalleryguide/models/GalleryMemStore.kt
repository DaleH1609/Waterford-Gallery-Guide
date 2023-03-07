package org.wit.waterfordgalleryguide.models

import timber.log.Timber.i

var lastId = 0L

internal fun getId(): Long {
    return lastId++
}

class GalleryMemStore : GalleryStore {

    val galleries = ArrayList<GalleryModel>()

    override fun findAll(): List<GalleryModel> {
        return galleries
    }

    override fun delete(gallery: GalleryModel) {
        galleries.remove(gallery)
    }

    override fun findById(id:Long) : GalleryModel? {
        val foundPlacemark: GalleryModel? = galleries.find { it.id == id }
        return foundPlacemark
    }

    override fun update(gallery: GalleryModel) {
        var foundGallery: GalleryModel? = galleries.find { p -> p.id == gallery.id }
        if (foundGallery != null) {
            foundGallery.title = gallery.title
            foundGallery.description = gallery.description
            foundGallery.image = gallery.image
            foundGallery.location = gallery.location
            logAll()
        }
    }

    override fun clear(){
        galleries.clear()
    }

    override fun create(gallery: GalleryModel) {
        gallery.id = getId()
        galleries.add(gallery)
        logAll()
    }

    fun logAll() {
        galleries.forEach{ i("${it}") }
    }
}