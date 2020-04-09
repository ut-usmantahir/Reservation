package com.example.reservation

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import com.example.reservation.databinding.ActivitySignupCustomerBinding
import com.google.android.gms.location.LocationListener
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth

class SignUpCustomer : AppCompatActivity(), OnMapReadyCallback, LocationListener{

//    lateinit var dialog: android.app.AlertDialog
    lateinit var mBinding : ActivitySignupCustomerBinding
    private lateinit var mMap: GoogleMap
    private lateinit var currentLocation: Location
    private lateinit var locationManager: LocationManager
    private var currentLocationMarker: Marker? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_signup_customer)

//        dialog = SpotsDialog.Builder().setCancelable(false).setContext(this).build()

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

       if (ActivityCompat.checkSelfPermission(
               this,
               Manifest.permission.ACCESS_FINE_LOCATION
           ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
               this,
               Manifest.permission.ACCESS_COARSE_LOCATION
           ) != PackageManager.PERMISSION_GRANTED
       ) {

           return
       }

       var location =  locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)

//        currentLocation?.latitude = location.latitude
//        currentLocation?.longitude = location.longitude
        currentLocation = Location(location)
        onLocationChanged(location)

        Log.d("Register","HomeCustomer, Inside On create ")

        mBinding.btnRequst.setOnClickListener {
            getCurrentLocation()
        }
        mBinding.btnLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()

            val intent = Intent(this@SignUpCustomer, MainActivity::class.java)
            startActivity(intent)
            finish()
        }


    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val currentPos = LatLng(currentLocation.latitude, currentLocation.longitude)
        mMap.addMarker(MarkerOptions().position(currentPos).title("Current Position"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentPos))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentPos, 15f))


    }

    override fun onLocationChanged(p0: Location?) {

        currentLocation?.latitude = p0?.latitude!!
        currentLocation?.longitude = p0?.longitude!!

    }

    private fun getCurrentLocation(){

            currentLocationMarker?.remove()

/*
        val lat: Double = 32.166351
        val lng: Double = 74.195900
*/
        val lat = currentLocation?.latitude
        val lng = currentLocation?.longitude


        val currentLocation = LatLng(lat!!, lng!!)

        currentLocationMarker = mMap.addMarker(
            MarkerOptions().position(
                LatLng(lat, lng)
            )
                .title("Your are Here").visible(true)
                .snippet("current location")

        )
//        currentLocationMarker.showInfoWindow()
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15f))

    }



}
