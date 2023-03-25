package org.wit.waterfordgalleryguide.models

interface AllGalleriesStore {
    fun findAll(): List<AllGalleriesModel>
    fun create(allGalleries: AllGalleriesModel)
}