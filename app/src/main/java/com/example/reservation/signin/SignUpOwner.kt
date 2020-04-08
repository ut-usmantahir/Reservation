package com.example.reservation.signin

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.reservation.HomeOwner
import com.example.reservation.R
import com.example.reservation.databinding.ActivitySignupOwnerBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import dmax.dialog.SpotsDialog
import java.util.*
import kotlin.collections.HashMap

class SignUpOwner : AppCompatActivity() {

    lateinit var dialog: android.app.AlertDialog
    private val mAuth = FirebaseAuth.getInstance()
//    lateinit var mDatabase : DatabaseReference
//    lateinit var mHotelReference: DatabaseReference
    private var imageUri: Uri? = null
    lateinit var homeOwnerBinding : ActivitySignupOwnerBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeOwnerBinding = DataBindingUtil.setContentView(this,
            R.layout.activity_signup_owner
        )

        dialog = SpotsDialog.Builder().setCancelable(false).setContext(this).build()

        Log.d("Register","HomeOwner, Inside On create ")

        homeOwnerBinding.btnRegister.setOnClickListener {
            Log.d("Register","HomeOwner: Inside Register Button")
            registerOwner()
        }


    }

    private fun registerOwner(){
        
        var hotelName           = homeOwnerBinding.txtHotelName.text.toString()
        var hotelRooms          = homeOwnerBinding.txtNumberOfRooms.text.toString()
        var roomsAvailable      = homeOwnerBinding.txtAvailableRooms.text.toString()
        var hotelDescription    = homeOwnerBinding.txtHotelDescription.text.toString()
        var hotelLocation       = homeOwnerBinding.txtLocation.text.toString()

        if (hotelName.isEmpty()) {
            homeOwnerBinding.txtHotelName!!.error = "Hotel Name is required"
            homeOwnerBinding.txtHotelName!!.requestFocus()
            return
        }
        if (hotelRooms.isEmpty()) {
            homeOwnerBinding.txtNumberOfRooms!!.error = "Number of Rooms is required"
            homeOwnerBinding.txtNumberOfRooms!!.requestFocus()
            return
        }
        if (roomsAvailable.isEmpty()) {
            homeOwnerBinding.txtAvailableRooms!!.error = "Available Rooms is required"
            homeOwnerBinding.txtAvailableRooms!!.requestFocus()
            return
        }
        if (hotelDescription.isEmpty()) {
            homeOwnerBinding.txtHotelDescription!!.error = "Hotel Description is required"
            homeOwnerBinding.txtHotelDescription!!.requestFocus()
            return
        }
        if (hotelLocation.isEmpty()) {
            homeOwnerBinding.txtLocation!!.error = "Hotel Location is required"
            homeOwnerBinding.txtLocation!!.requestFocus()
            return
        }




        Log.d("Register","HomeOwner, Inside On registerOwner ")

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

               val mDatabase = FirebaseDatabase.getInstance().reference.child("users").child("usertype").child("owner").child(uid)
               val mHotelReference = FirebaseDatabase.getInstance().reference.child("hotels").child(uid)

                val userHashMap: MutableMap<String, Any> = HashMap()
                val hotelHashMap: MutableMap<String, Any> = HashMap()

                userHashMap["firstname"] = "" + firstname
                userHashMap["lastname"] = "" + lastname
                userHashMap["username"] = "" + username
                userHashMap["email"] = "" + email
                userHashMap["phonenumber"] = "" + phonenumber
                userHashMap["usertype"] = "" + userType
                userHashMap["pass"] = "" + password     //just for testing, saving password

                hotelHashMap["hotelName"] = "" + hotelName
                hotelHashMap["hotelRooms"] = "" + hotelRooms
                hotelHashMap["roomsAvailable"] = "" + roomsAvailable
                hotelHashMap["hotelDescription"] = "" + hotelDescription
                hotelHashMap["hotelLocation"] = "" + hotelLocation

                mDatabase.updateChildren(userHashMap)
                mHotelReference.updateChildren(hotelHashMap)

                uploadImageToFirebase()
                dialog.dismiss()
                startActivity(Intent(this, HomeOwner::class.java))

                Toast.makeText(this, "Account Created Successfully :)", Toast.LENGTH_LONG).show()
            }else {
                dialog.dismiss()
                Toast.makeText(this, "Error registering, try again later :(", Toast.LENGTH_LONG).show()
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

        val ref = FirebaseDatabase.getInstance().reference.child("users").child("usertype").child("owner").child(uid)

        val imageUrlHashmap: MutableMap<String, Any> = HashMap()

        imageUrlHashmap["imageUrl"] = "" + profileImageUrl

        ref.updateChildren(imageUrlHashmap)

    }


}
