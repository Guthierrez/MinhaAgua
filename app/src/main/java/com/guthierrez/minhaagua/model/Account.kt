package com.guthierrez.minhaagua.model

import java.util.*

data class Account(val number: String?, val cpf: String?, val birthDate: Date?) {
    object KEY {
        const val USER = "user"
        const val ACCOUNT = "account"
        const val BIRTH_DATE = "birthDate"
        const val CPF = "cpf"
        const val AVERAGE_CONSUMPTION = "averageConsumption"
        const val MINIMUM_CONSUMPTION = "minimumConsumption"
        const val MAXIMUM_CONSUMPTION = "maximumConsumption"
    }

    var averageConsumption: Long? = null
    var minimumConsumption: Long? = null
    var maximumConsumption: Long? = null
}