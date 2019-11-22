package com.guthierrez.minhaagua.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.guthierrez.minhaagua.R
import com.guthierrez.minhaagua.model.LeakStep
import com.guthierrez.minhaagua.ui.viewholder.LeakFlowViewHolder

class LeakFlowAdapter(
        private val leakSteps: List<LeakStep>
) : RecyclerView.Adapter<LeakFlowViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeakFlowViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.leak_step_item, parent, false)
        return LeakFlowViewHolder(view)
    }

    override fun getItemCount(): Int {
        return leakSteps.count()
    }

    override fun onBindViewHolder(holder: LeakFlowViewHolder, position: Int) {
        val leakStep = leakSteps[position]
        holder.bindData(leakStep)
    }

}