package com.guthierrez.minhaagua.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.guthierrez.minhaagua.R
import com.guthierrez.minhaagua.listeners.LeakListEventListener
import com.guthierrez.minhaagua.model.Leak
import com.guthierrez.minhaagua.ui.viewholder.LeakListViewHolder

class LeakListAdapter(
        private val leakList: List<Leak>,
        private val leakListEventListener: LeakListEventListener
) : RecyclerView.Adapter<LeakListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeakListViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.leak_list_item, parent, false)
        return LeakListViewHolder(view, leakListEventListener, context)
    }

    override fun getItemCount(): Int {
        return leakList.count()
    }

    override fun onBindViewHolder(holder: LeakListViewHolder, position: Int) {
        val leak = leakList[position]
        holder.bindData(leak)
    }

}