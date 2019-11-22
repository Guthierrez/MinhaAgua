package com.guthierrez.minhaagua.repository

import com.guthierrez.minhaagua.auth.FirebaseAuthenticationManager
import com.guthierrez.minhaagua.model.User
import org.apache.commons.lang3.StringUtils

class UserRepository private constructor() {
    private var firebaseAuthenticationManager = FirebaseAuthenticationManager.getInstance()

    companion object {
        fun getInstance(): UserRepository {
            if (INSTANCE == null) {
                INSTANCE = UserRepository()
            }
            return INSTANCE as UserRepository
        }

        private var INSTANCE: UserRepository? = null
    }

    fun insert(name: String, email: String, password: String, onResult: (Boolean) -> Unit) {
        firebaseAuthenticationManager.register(email, password, name, onResult)
    }

    fun isEmailExistent(email: String): Boolean {
        return StringUtils.isNotBlank(email)
    }
}