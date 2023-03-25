package org.wit.waterfordgalleryguide.models

import timber.log.Timber.i

var galleryId = 0L

internal fun getAllId(): Long {
    return galleryId++
}


class AllGalleriesMemStore : AllGalleriesStore {

    val allGalleries = ArrayList<AllGalleriesModel>()

    override fun findAll(): List<AllGalleriesModel> {
        return allGalleries
    }

    override fun create(galleries: AllGalleriesModel) {
        galleries.id = getAllId()
        allGalleries.add(galleries)
        logAll()
    }

    private fun logAll() {
        allGalleries.forEach { i("$it") }
    }
}