package com.guthierrez.minhaagua.business

import com.guthierrez.minhaagua.exception.ValidationException
import com.guthierrez.minhaagua.model.Leak
import com.guthierrez.minhaagua.repository.LeakRepository
import org.apache.commons.lang3.StringUtils
import java.lang.Exception

class LeakBusiness {
    private val leakRepository = LeakRepository()

    fun saveLeak(leak: Leak, onFailure: (ex: Exception) -> Unit, onSucess: (leakImage: Leak) -> Unit) {
        if (StringUtils.isBlank(leak.description)) {
            throw ValidationException("Informe a descrição do vazamento.")
        }
        leakRepository.saveLeak(leak, onFailure, onSucess)
    }

    fun listLeaks(user: String, onFailure: (ex: Exception) -> Unit, onSucess: (leakImage: MutableList<Leak>) -> Unit) {
        leakRepository.listLeaks(user, onFailure, onSucess)
    }
}