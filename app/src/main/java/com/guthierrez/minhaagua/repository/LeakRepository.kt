package com.guthierrez.minhaagua.repository

import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.FirebaseFirestore
import com.guthierrez.minhaagua.constants.DBConstants
import com.guthierrez.minhaagua.model.Leak
import com.guthierrez.minhaagua.model.LeakImage
import com.guthierrez.minhaagua.model.LeakStep
import com.guthierrez.minhaagua.model.StepStatus
import com.guthierrez.minhaagua.util.date
import com.guthierrez.minhaagua.util.timestamp
import java.lang.Exception
import java.util.*

class LeakRepository {

    private val firestore = FirebaseFirestore.getInstance()

    fun saveLeak(leak: Leak, onFailure: (ex: Exception) -> Unit, onSucess: (leak: Leak) -> Unit) {
        val leakMap = hashMapOf(
                Leak.KEY.DESCRIPTION to leak.description,
                "latitude" to leak.location.latitude,
                "longitude" to leak.location.longitude,
                "images" to leak.leakImages.filter { it.id != null }.map { it.id },
                Leak.KEY.USER to leak.user,
                "date" to Date().timestamp()
        )

        firestore.collection(DBConstants.COLLECTIONS.LEAKS)
                .add(leakMap)
                .addOnSuccessListener {
                    onSucess(leak)
                }.addOnFailureListener {
                    onFailure(it)
                }
    }

    fun listLeaks(user: String, onFailure: (ex: Exception) -> Unit, onSucess: (leakImage: MutableList<Leak>) -> Unit) {
        firestore.collection(DBConstants.COLLECTIONS.LEAKS)
                .whereEqualTo(Leak.KEY.USER, user)
                .get()
                .addOnSuccessListener { querySnapShot ->
                    val leakList = querySnapShot.documents.map { document ->
                        val leak = Leak(
                                document.get(Leak.KEY.DESCRIPTION) as String,
                                document.get(Leak.KEY.IMAGES) as MutableList<LeakImage>,
                                LatLng(document.getDouble(Leak.KEY.LATITUDE)!!, document.getDouble(Leak.KEY.LONGITUDE)!!),
                                document.getString(Leak.KEY.USER)!!
                        )
                        leak.date = (document.get(Leak.KEY.DATE) as String).date()
                        leak.id = document.id
                        fillLeakSteps(leak)
                        leak
                    }
                    onSucess(leakList.toMutableList())
                }
                .addOnFailureListener {
                    onFailure(it)
                }
    }

    private fun fillLeakSteps(leak: Leak) {
        firestore.collection(DBConstants.COLLECTIONS.STEPS)
                .get()
                .addOnSuccessListener { querySnapShot ->
                    val stepList = querySnapShot.documents.map { document ->
                        val leakStep = LeakStep(
                                document.id,
                                document.get(LeakStep.KEY.DESCRIPTION) as String
                        )
                        fillStepData(leak, leakStep)
                        leakStep
                    }
                    stepList.sortedBy { it.step.toInt() }
                    leak.leakSteps = stepList
                }

    }

    private fun fillStepData(leak: Leak, leakStep: LeakStep) {
        firestore.collection(DBConstants.COLLECTIONS.LEAK_FLOW)
                .whereEqualTo(LeakStep.KEY.LEAK, leak.id)
                .whereEqualTo(LeakStep.KEY.STEP, leakStep.step)
                .get()
                .addOnSuccessListener { querySnapShot ->
                    querySnapShot.documents.map { document ->
                        val step = document.get(LeakStep.KEY.STEP) as String
                        if (step == leakStep.step) {
                            leakStep.status = StepStatus.values().find { it.value == document.get(LeakStep.KEY.STATUS) as String }
                            leakStep.note = document.get(LeakStep.KEY.NOTE) as String
                        }
                    }
                }
    }
}