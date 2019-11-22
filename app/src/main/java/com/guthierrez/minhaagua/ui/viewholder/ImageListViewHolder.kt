package com.guthierrez.minhaagua.ui.viewholder

import android.app.AlertDialog
import android.content.Context
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.guthierrez.minhaagua.R
import com.guthierrez.minhaagua.listeners.ImageListEventListener
import com.guthierrez.minhaagua.model.LeakImage

class ImageListViewHolder(
        itemView: View,
        private val imageListEventListener: ImageListEventListener,
        private val context: Context
) : RecyclerView.ViewHolder(itemView) {

    private val imagemItem: ImageView = itemView.findViewById(R.id.imagemItem)

    fun bindData(leakImage: LeakImage) {
        imagemItem.setImageURI(leakImage.uri)
        imagemItem.setOnClickListener {
            handleDelete(leakImage)
        }
    }

    private fun handleDelete(leakImage: LeakImage) {
        AlertDialog.Builder(context)
                .setTitle("Remoção de tarefa")
                .setMessage("Deseja remover a imagem?")
                .setIcon(R.drawable.ic_done)
                .setNegativeButton("Cancelar", null)
                .setPositiveButton("Remover") { _, _ ->
                    imageListEventListener.onImageDelete(leakImage)
                }
                .show()
    }
}