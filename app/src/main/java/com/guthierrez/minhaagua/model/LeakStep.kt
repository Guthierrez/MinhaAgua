package com.guthierrez.minhaagua.model

import java.io.Serializable

data class LeakStep(val step: String, val description: String): Serializable {

    object KEY {
        const val STEP = "step"
        const val DESCRIPTION = "description"
        const val LEAK = "leak"
        const val STATUS = "status"
        const val NOTE = "note"
    }

    var status: StepStatus? = null
    var note: String? = null
}
