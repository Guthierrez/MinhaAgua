package com.guthierrez.minhaagua.listeners

import com.guthierrez.minhaagua.model.LeakImage

interface ImageListEventListener {
    fun onImageDelete(leakImage: LeakImage)
}