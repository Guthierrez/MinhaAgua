package com.guthierrez.minhaagua.ui.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.guthierrez.minhaagua.R
import com.guthierrez.minhaagua.model.LeakStep
import com.guthierrez.minhaagua.model.StepStatus

class LeakFlowViewHolder(
        itemView: View
) : RecyclerView.ViewHolder(itemView) {

    private val leakStepImage: ImageView = itemView.findViewById(R.id.leakStepImage)
    private val leakStepIndex: TextView = itemView.findViewById(R.id.leakStepIndex)
    private val leakStepDescription: TextView = itemView.findViewById(R.id.leakStepDescription)
    private val leakStepNote: TextView = itemView.findViewById(R.id.leakStepNote)

    fun bindData(leakStep: LeakStep) {
        when {
            leakStep.step == "1" || leakStep.status == StepStatus.EXECUTED -> leakStepImage.setImageResource(R.drawable.ic_done)
            leakStep.status == StepStatus.CANCELED -> leakStepImage.setImageResource(R.drawable.ic_error)
            leakStep.status == StepStatus.IN_PROGRESS -> leakStepImage.setImageResource(R.drawable.ic_waiting)
            else -> leakStepImage.setImageResource(R.drawable.ic_todo)
        }
        leakStepIndex.text = leakStep.step + "."
        leakStepDescription.text = leakStep.description
        leakStepNote.text = leakStep.note
    }
}