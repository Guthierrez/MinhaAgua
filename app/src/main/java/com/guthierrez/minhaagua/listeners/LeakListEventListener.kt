package com.guthierrez.minhaagua.listeners

import com.guthierrez.minhaagua.model.Leak

interface LeakListEventListener {
    fun onLeakSelection(leak: Leak)
}