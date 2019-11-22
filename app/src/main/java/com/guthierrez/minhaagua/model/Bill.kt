package com.guthierrez.minhaagua.model

import java.util.*

data class Bill(var account: String?, var date: Date?, var value: Double?, var consumption: Long?, var payed: Boolean?) {
    object KEY {
        const val ACCOUNT = "account"
        const val CONSUMPTION = "consumption"
        const val DATE = "date"
        const val VALUE = "value"
        const val PAYED = "payed"
    }
}