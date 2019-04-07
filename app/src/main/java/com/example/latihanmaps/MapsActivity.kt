package com.example.latihanmaps

import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Location
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_maps.*


class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    override fun onMarkerClick(p0: Marker?) = false

    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location
    private var isFABOpen = false

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    private fun showFABMenu() {
        isFABOpen = true
        tv1.visibility = VISIBLE
        tv2.visibility = VISIBLE
        tv3.visibility = VISIBLE
        tv4.visibility = VISIBLE
        fab1.animate().translationY(-resources.getDimension(R.dimen.standard_60))
        fab2.animate().translationY(-resources.getDimension(R.dimen.standard_120))
        fab3.animate().translationY(-resources.getDimension(R.dimen.standard_180))
        fab4.animate().translationY(-resources.getDimension(R.dimen.standard_240))
        tv1.animate().translationY(-resources.getDimension(R.dimen.standard_60))
        tv2.animate().translationY(-resources.getDimension(R.dimen.standard_120))
        tv3.animate().translationY(-resources.getDimension(R.dimen.standard_180))
        tv4.animate().translationY(-resources.getDimension(R.dimen.standard_240))
    }

    private fun closeFABMenu() {
        isFABOpen = false
        tv1.visibility = INVISIBLE
        tv2.visibility = INVISIBLE
        tv3.visibility = INVISIBLE
        tv4.visibility = INVISIBLE
        fab1.animate().translationY(0f)
        fab2.animate().translationY(0f)
        fab3.animate().translationY(0f)
        fab4.animate().translationY(0f)
        tv1.animate().translationY(0f)
        tv2.animate().translationY(0f)
        tv3.animate().translationY(0f)
        tv4.animate().translationY(0f)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-6.1646775, 106.7650362)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Kedoya"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 18.9f))

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.setOnMarkerClickListener(this)
        setUpMap()

        fab.setOnClickListener {
            if (!isFABOpen) {
                showFABMenu()
            } else {
                closeFABMenu()
            }
        }

        fab1.setOnClickListener {
            tv1.setTextColor(resources.getColor(R.color.black))
            tv2.setTextColor(resources.getColor(R.color.black))
            tv3.setTextColor(resources.getColor(R.color.black))
            tv4.setTextColor(resources.getColor(R.color.black))
            mMap.mapType = GoogleMap.MAP_TYPE_TERRAIN
        }
        fab2.setOnClickListener {
            tv1.setTextColor(resources.getColor(R.color.white))
            tv2.setTextColor(resources.getColor(R.color.white))
            tv3.setTextColor(resources.getColor(R.color.white))
            tv4.setTextColor(resources.getColor(R.color.white))
            mMap.mapType = GoogleMap.MAP_TYPE_HYBRID
        }
        fab3.setOnClickListener {
            tv1.setTextColor(resources.getColor(R.color.white))
            tv2.setTextColor(resources.getColor(R.color.white))
            tv3.setTextColor(resources.getColor(R.color.white))
            tv4.setTextColor(resources.getColor(R.color.white))
            mMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
        }
        fab4.setOnClickListener {
            tv1.setTextColor(resources.getColor(R.color.black))
            tv2.setTextColor(resources.getColor(R.color.black))
            tv3.setTextColor(resources.getColor(R.color.black))
            tv4.setTextColor(resources.getColor(R.color.black))
            mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
        }
    }

    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }

        mMap.isMyLocationEnabled = true
        fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
            if (location != null) {
                lastLocation = location
                val currentPos = LatLng(location.latitude, location.longitude)
                placeMarkerInMap(currentPos)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentPos, 18.0F))
            }
        }
    }

    private fun placeMarkerInMap(loc: LatLng) {
        val markerOptions = MarkerOptions().position(loc)
        markerOptions.icon(
            BitmapDescriptorFactory.fromBitmap(
                BitmapFactory.decodeResource(resources, R.mipmap.person)
            )
        )
        mMap.addMarker(markerOptions)
    }
}
