package com.guthierrez.minhaagua.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.guthierrez.minhaagua.R
import com.guthierrez.minhaagua.listeners.ImageListEventListener
import com.guthierrez.minhaagua.model.LeakImage
import com.guthierrez.minhaagua.ui.viewholder.ImageListViewHolder

class ImageListAdapter(
        private val imageList: MutableList<LeakImage>,
        private val imageListEventListener: ImageListEventListener
) : RecyclerView.Adapter<ImageListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageListViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.image_list_item, parent, false)
        return ImageListViewHolder(view, imageListEventListener, context)
    }

    override fun getItemCount(): Int {
        return imageList.count()
    }

    override fun onBindViewHolder(holder: ImageListViewHolder, position: Int) {
        val image = imageList[position]
        holder.bindData(image)
    }

}