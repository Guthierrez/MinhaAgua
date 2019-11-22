package com.guthierrez.minhaagua.auth

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

class FirebaseAuthenticationManager private constructor() {

    private val authentication: FirebaseAuth = FirebaseAuth.getInstance()

    companion object {
        fun getInstance(): FirebaseAuthenticationManager {
            if (INSTANCE == null) {
                INSTANCE = FirebaseAuthenticationManager()
            }
            return INSTANCE as FirebaseAuthenticationManager
        }

        private var INSTANCE: FirebaseAuthenticationManager? = null
    }

    fun register(email: String, password: String, userName: String, onResult: (Boolean) -> Unit) {
        authentication.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isComplete && it.isSuccessful) {
                authentication.currentUser?.updateProfile(
                        UserProfileChangeRequest
                                .Builder()
                                .setDisplayName(userName)
                                .build()
                )
                onResult(true)
            } else {
                onResult(false)
            }
        }
    }

    fun login(email: String, password: String, onResult: (Boolean) -> Unit) {
        authentication.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            onResult(it.isComplete && it.isSuccessful)
        }
    }

    fun getUserId(): String = authentication.currentUser?.uid ?: ""
    fun getUserName(): String = authentication.currentUser?.displayName ?: ""
    fun getUserEmail(): String = authentication.currentUser?.email ?: ""

    fun logOut() {
        authentication.signOut()
    }
}