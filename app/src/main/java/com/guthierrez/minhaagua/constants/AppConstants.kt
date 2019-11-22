package com.guthierrez.minhaagua.constants

class AppConstants private constructor() {
    object KEY {
        const val USER_ID = "userId"
        const val USER_NAME = "userName"
        const val USER_EMAIL = "userEmail"
        const val LEAK = "leak"
        const val ACCOUNT = "account"
    }

    object LEAKFILTER {
        const val KEY =  "leaklistfilter"
        const val COMPLETE = 1
        const val TODO = 0
    }
}
