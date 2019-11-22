package com.guthierrez.minhaagua.business

import com.google.firebase.storage.FirebaseStorage
import com.guthierrez.minhaagua.model.LeakImage
import java.util.*

class ImageUploaderBusiness {
    private val firebaseStorage = FirebaseStorage.getInstance()
    private val storageReference = firebaseStorage.reference

    fun uploadImage(leakImage: LeakImage) {
        val imageId = UUID.randomUUID().toString()
        val ref = storageReference.child("images/$imageId")
        ref.putFile(leakImage.uri)
                .addOnSuccessListener {
                    leakImage.id = imageId
                }
    }

    fun deleteUploadedImage(
            leakImage: LeakImage,
            onImageDeleted: (leakImage: LeakImage) -> Unit
    ) {
        val ref = storageReference.child("images/${leakImage.id}")
        ref.delete().addOnSuccessListener {
            onImageDeleted(leakImage)
        }
    }
}