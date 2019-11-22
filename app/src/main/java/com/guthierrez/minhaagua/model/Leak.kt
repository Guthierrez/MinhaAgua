package com.guthierrez.minhaagua.model

import com.google.android.gms.maps.model.LatLng
import java.io.Serializable
import java.util.*
import com.google.android.gms.common.internal.safeparcel.SafeParcelReader.readDouble
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter.writeDouble
import java.io.IOException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream


data class Leak(var description: String, var leakImages: MutableList<LeakImage>, @Transient var location: LatLng, val user: String): Serializable {

    object KEY {
        const val USER = "user"
        const val DATE = "date"
        const val DESCRIPTION = "description"
        const val IMAGES = "images"
        const val LATITUDE = "latitude"
        const val LONGITUDE = "longitude"
    }

    var id: String? = null
    var date: Date? = null
    var leakSteps: List<LeakStep> = mutableListOf()

    @Throws(IOException::class)
    private fun writeObject(out: ObjectOutputStream) {
        out.defaultWriteObject()
        out.writeDouble(location.latitude)
        out.writeDouble(location.longitude)
    }

    @Throws(IOException::class, ClassNotFoundException::class)
    private fun readObject(`in`: ObjectInputStream) {
        `in`.defaultReadObject()
        location = LatLng(`in`.readDouble(), `in`.readDouble())
    }
}