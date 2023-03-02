package org.wit.waterfordgalleryguide.models

import android.content.Context
import android.net.Uri
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import org.wit.waterfordgalleryguide.helpers.*
import timber.log.Timber
import java.lang.reflect.Type
import java.util.*

const val JSON_FILE = "galleries.json"
val gsonBuilder: Gson = GsonBuilder().setPrettyPrinting()
    .registerTypeAdapter(Uri::class.java, UriParser())
    .create()
val listType: Type = object : TypeToken<ArrayList<GalleryModel>>() {}.type

fun generateRandomId(): Long {
    return Random().nextLong()
}

class GalleryJSONStore(private val context: Context) : GalleryStore {

    var galleries = mutableListOf<GalleryModel>()

    init {
        if (exists(context, JSON_FILE)) {
            deserialize()
        }
    }

    override fun findAll(): MutableList<GalleryModel> {
        logAll()
        return galleries
    }

    override fun create(gallery: GalleryModel) {
        gallery.id = generateRandomId()
        galleries.add(gallery)
        serialize()
    }

    override fun delete(gallery: GalleryModel) {
        galleries.remove(gallery)
        serialize()
    }

    override fun findById(id:Long) : GalleryModel? {
        val foundPlacemark: GalleryModel? = galleries.find { it.id == id }
        return foundPlacemark
    }

    override fun update(gallery: GalleryModel) {
        val galleriesList = findAll() as ArrayList<GalleryModel>
        var foundGallery: GalleryModel? = galleriesList.find { p -> p.id == gallery.id }
        if (foundGallery != null) {
            foundGallery.title = gallery.title
            foundGallery.description = gallery.description
            foundGallery.image = gallery.image
            foundGallery.lat = gallery.lat
            foundGallery.lng = gallery.lng
            foundGallery.zoom = gallery.zoom
        }
        serialize()
    }

    private fun serialize() {
        val jsonString = gsonBuilder.toJson(galleries, listType)
        write(context, JSON_FILE, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, JSON_FILE)
        galleries = gsonBuilder.fromJson(jsonString, listType)
    }

    private fun logAll() {
        galleries.forEach { Timber.i("$it") }
    }
}

class UriParser : JsonDeserializer<Uri>,JsonSerializer<Uri> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Uri {
        return Uri.parse(json?.asString)
    }

    override fun serialize(
        src: Uri?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return JsonPrimitive(src.toString())
    }
}