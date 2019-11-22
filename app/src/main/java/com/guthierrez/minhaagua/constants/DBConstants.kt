package com.guthierrez.minhaagua.constants

class DBConstants private constructor() {

    object COLLECTIONS {
        const val LEAKS = "leaks"
        const val STEPS = "steps"
        const val LEAK_FLOW = "leak_flow"
        const val ACCOUNT = "account"
        const val BILLS = "bills"
        const val USER_ACCOUNT = "user_account"
    }

    object USER {
        const val TABLE_NAME = "user"

        object COLUMNS {
            const val ID = "user"
            const val NAME = "name"
            const val EMAIL = "email"
            const val PASSWORD = "password"
        }
    }

    object PRIORITY {
        const val TABLE_NAME = "priority"

        object COLUMNS {
            const val ID = "id"
            const val DESCRIPTION = "description"
        }
    }

    object TASK {
        const val TABLE_NAME = "task"

        object COLUMNS {
            const val ID = "id"
            const val USER_ID = "user_id"
            const val PRIORITY_ID = "priority_id"
            const val DESCRIPTION = "description"
            const val COMPLETE = "complete"
            const val DUE_DATE = "due_date"
        }
    }
}