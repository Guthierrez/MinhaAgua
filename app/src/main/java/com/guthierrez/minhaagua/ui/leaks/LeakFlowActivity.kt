package com.guthierrez.minhaagua.ui.leaks

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MarkerOptions
import com.guthierrez.minhaagua.R
import com.guthierrez.minhaagua.constants.AppConstants
import com.guthierrez.minhaagua.model.Leak
import com.guthierrez.minhaagua.ui.adapter.LeakFlowAdapter
import com.guthierrez.minhaagua.util.timestamp
import kotlinx.android.synthetic.main.activity_leak_flow.*

class LeakFlowActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var recyclerLeakFlow: RecyclerView
    private lateinit var leak: Leak

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leak_flow)

        leak = intent.extras?.getSerializable(AppConstants.KEY.LEAK) as Leak

        recyclerLeakFlow = findViewById(R.id.recyclerLeakFlow)
        recyclerLeakFlow.layoutManager = LinearLayoutManager(this)
        recyclerLeakFlow.adapter = LeakFlowAdapter(leak.leakSteps)

        loadLocation()
        fillData()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        googleMap.addMarker(MarkerOptions().position(leak.location))
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(leak.location, 15F))
    }

    private fun loadLocation() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
    }

    private fun fillData() {
        textLeakDescription.text = leak.description
        textLeakDate.text = "Data: " + leak.date?.timestamp()
    }
}
