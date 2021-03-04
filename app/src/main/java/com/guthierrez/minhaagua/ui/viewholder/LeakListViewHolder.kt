package com.guthierrez.minhaagua.ui.viewholder

import android.content.Context
import android.os.Handler
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.guthierrez.minhaagua.R
import com.guthierrez.minhaagua.listeners.LeakListEventListener
import com.guthierrez.minhaagua.model.Leak
import com.guthierrez.minhaagua.model.StepStatus
import com.guthierrez.minhaagua.util.timestamp


class LeakListViewHolder(
        itemView: View,
        private val leakListEventListener: LeakListEventListener,
        private val context: Context
) : RecyclerView.ViewHolder(itemView) {

    private val leakLitemItem: RelativeLayout = itemView.findViewById(R.id.leakLitemItem)
    private val textDescription: TextView = itemView.findViewById(R.id.leakItemDescription)
    private val textLeakDate: TextView = itemView.findViewById(R.id.leakItemDate)
    private val imageItem: ImageView = itemView.findViewById(R.id.leakItemImage)

    fun bindData(leak: Leak) {
        leakLitemItem.setOnClickListener { leakListEventListener.onLeakSelection(leak) }

        textDescription.text = leak.description
        textLeakDate.text = leak.date!!.timestamp()

        val handler = Handler()
        handler.postDelayed(object : Runnable {
            override fun run() {
                val lastStep = leak.leakSteps.findLast { it.status != null }
                if (lastStep != null) {
                    when {
                        lastStep.status == StepStatus.EXECUTED -> imageItem.setImageResource(R.drawable.ic_done)
                        lastStep.status == StepStatus.CANCELED -> imageItem.setImageResource(R.drawable.ic_error)
                        lastStep.status == StepStatus.IN_PROGRESS -> imageItem.setImageResource(R.drawable.ic_waiting)
                    }
                } else {
                    imageItem.setImageResource(R.drawable.ic_waiting)
                }
                println("running")
                handler.postDelayed(this, 1000)
            }
        }, 500)
    }
}