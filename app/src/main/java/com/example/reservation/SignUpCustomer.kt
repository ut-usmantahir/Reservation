package com.example.reservation

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.getSystemService
import androidx.databinding.DataBindingUtil
import com.example.reservation.databinding.ActivitySignupCustomerBinding
import com.example.reservation.signin.SignUp
import com.google.android.gms.location.LocationListener
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import dmax.dialog.SpotsDialog
import java.util.*
import kotlin.collections.HashMap

class SignUpCustomer : AppCompatActivity(), OnMapReadyCallback, LocationListener{


    lateinit var dialog: android.app.AlertDialog
    val mAuth = FirebaseAuth.getInstance()
    lateinit var mDatabase : DatabaseReference
    private var imageUri: Uri? = null

    lateinit var mBinding : ActivitySignupCustomerBinding

    private lateinit var mMap: GoogleMap
    private lateinit var currentLocation: Location
    private lateinit var locationManager: LocationManager
    private var currentLocationMarker: Marker? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_signup_customer)

        dialog = SpotsDialog.Builder().setCancelable(false).setContext(this).build()

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

        registerCustomer()

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
    private fun registerCustomer(){

        Log.d("Register","Inside On registerCustomer Home Customer")

        val bundle = intent.extras

        var firstname = bundle?.get("firstname").toString()
        var lastname = bundle?.get("lastname").toString()
        var username = bundle?.get("username").toString()
        var email = bundle?.get("email").toString()
        var phonenumber = bundle?.get("phonenumber").toString()
        var userType = bundle?.get("userType").toString()
        var password = bundle?.get("password").toString()

//      Uri.parse(extras.getString("KEY"));

        imageUri = Uri.parse(bundle?.get("imageUri") as String?)


        dialog.show()
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                Log.d("Register", "Inside of createUserWithEmailAndPassword")

                val user = task.result?.user
                val uid = user!!.uid

                mDatabase = FirebaseDatabase.getInstance().reference.child("users").child("usertype").child("customer").child(uid)

                val userhashmap: MutableMap<String, Any> = HashMap()

                userhashmap["firstname"] = "" + firstname
                userhashmap["lastname"] = "" + lastname
                userhashmap["username"] = "" + username
                userhashmap["email"] = "" + email
                userhashmap["phonenumber"] = "" + phonenumber
                userhashmap["usertype"] = "" + userType
                userhashmap["pass"] = "" + password     //just for testing, saving password

                mDatabase.updateChildren(userhashmap)

                uploadImageToFirebase()
                dialog.dismiss()
//                startActivity(Intent(this, HomeCustomer::class.java))
//              On map ready function call here

                Toast.makeText(this, "Account Created Successfully :)", Toast.LENGTH_LONG).show()
            }else {
                dialog.dismiss()
                Toast.makeText(this, "Error registering, try again later :(", Toast.LENGTH_LONG).show()
                                startActivity(Intent(this, SignUp::class.java))

            }
        }

    }

    private fun uploadImageToFirebase(){

        Log.d("Register","Inside Upload To Firebase")

        if (imageUri == null) return

        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")
        Log.d("Register","Inside Upload To Firebase after ref")

        ref.putFile(imageUri!!)
            .addOnSuccessListener {
                Log.d("Register", "Successfully uploaded images: ${it.metadata?.path}")
                ref.downloadUrl.addOnSuccessListener {
                    Log.d("Register", "File Location: ${it}")
                    saveImageIntoFirebase(it.toString())
                }
            }


    }

    private fun saveImageIntoFirebase(profileImageUrl: String){
        Log.d("Register", "File Location Inside saveImageIntoFirebase: $profileImageUrl")
        val uid = mAuth.currentUser?.uid.toString()

        val ref = FirebaseDatabase.getInstance().reference.child("users").child("usertype").child("customer").child(uid)

        val imageUrlHashmap: MutableMap<String, Any> = HashMap()

        imageUrlHashmap["imageUrl"] = "" + profileImageUrl

        ref.updateChildren(imageUrlHashmap)

    }



}
