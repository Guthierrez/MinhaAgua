package com.guthierrez.minhaagua.business

import android.content.Context
import com.guthierrez.minhaagua.exception.ValidationException
import com.guthierrez.minhaagua.repository.UserRepository

class UserBusiness(val context: Context) {

    private val userRepository = UserRepository.getInstance()

    fun insert(name: String, email: String, password: String, onResult: (Boolean) -> Unit) {
        if (userRepository.isEmailExistent(email)) {
            throw ValidationException("Email já cadastrado.")
        }

        userRepository.insert(name, email, password, onResult)
    }
}