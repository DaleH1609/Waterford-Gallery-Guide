package org.wit.waterfordgalleryguide.models

import android.content.Context
import android.net.Uri
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import org.wit.waterfordgalleryguide.helpers.*
import timber.log.Timber
import java.lang.reflect.Type
import java.util.*

const val AJSON_FILE = "allGalleries.json"
val AgsonBuilder: Gson = GsonBuilder().setPrettyPrinting()
    .registerTypeAdapter(Uri::class.java, AllGalleriesJSONStore.UriParser())
    .create()

val AlistType: Type = object : TypeToken<ArrayList<AllGalleriesModel>>() {}.type

fun generateRandomAId(): Long {
    return Random().nextLong()
}

class AllGalleriesJSONStore(private val context: Context) : AllGalleriesStore {

    var allGalleries = mutableListOf<AllGalleriesModel>()

    init {
        if (exists(context, AJSON_FILE)) {
            deserialize()
        }
    }

    override fun findAll(): MutableList<AllGalleriesModel> {
        logAll()
        return allGalleries
    }

    override fun create(galleries: AllGalleriesModel) {
        galleries.id = generateRandomAId()
        allGalleries.add(galleries)
        serialize()
    }


    fun update(galleries: AllGalleriesModel) {
        // todo
    }

    private fun serialize() {
        val jsonString = AgsonBuilder.toJson(allGalleries, AlistType)
        write(context, AJSON_FILE, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, AJSON_FILE)
        allGalleries = AgsonBuilder.fromJson(jsonString, AlistType)
    }

    private fun logAll() {
        allGalleries.forEach { Timber.i("$it") }
    }


    class UriParser : JsonDeserializer<Uri>, JsonSerializer<Uri> {
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
}