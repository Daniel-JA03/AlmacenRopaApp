package com.rafael0117.appgestionalmacen

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.FragmentManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.rafael0117.appgestionalmacen.controller.SedeController
import com.rafael0117.appgestionalmacen.entidad.Sede
import com.rafael0117.appgestionalmacen.entidad.SedeConCant

class Mapa : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mapa)
        createFragment()
    }

    private fun createFragment() {
        val mapFragment : SupportMapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        // Obtener sede y sus ubicaciones
        val sede = intent.getSerializableExtra("sede") as SedeConCant
        val ubicaciones = SedeController().obtenerUbicacionesPorSede(sede.codigo)

        for (ubicacion in ubicaciones) {
            val coordenadas = LatLng(ubicacion.latitud, ubicacion.longitud)
            map.addMarker(MarkerOptions().position(coordenadas).title(ubicacion.descripcion))
        }

        // Centrar el mapa en el primer punto si hay
        if (ubicaciones.isNotEmpty()) {
            val primera = ubicaciones[0]
            val center = LatLng(primera.latitud, primera.longitud)
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(center, 15f))
        }
    }

}


