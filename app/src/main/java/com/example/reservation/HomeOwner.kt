package com.example.reservation

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.reservation.databinding.ActivityHomeOwnerBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import dmax.dialog.SpotsDialog
import java.util.*
import kotlin.collections.HashMap

class HomeOwner : AppCompatActivity() {

    lateinit var dialog: android.app.AlertDialog
    val mAuth = FirebaseAuth.getInstance()
    lateinit var mDatabase : DatabaseReference
    private var imageUri: Uri? = null
    lateinit var mBinding : ActivityHomeOwnerBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // setContentView(R.layout.activity_home_owner)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_home_owner)

        dialog = SpotsDialog.Builder().setCancelable(false).setContext(this).build()

        Log.d("Register","Inside On create Home Owner")
        registerOwner()

    }

    private fun registerOwner(){

        Log.d("Register","Inside On registerOwner Home Owner")

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

                mDatabase = FirebaseDatabase.getInstance().reference.child("users").child("usertype").child(uid)

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
                startActivity(Intent(this, HomeCustomer::class.java))

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

        val ref = FirebaseDatabase.getInstance().reference.child("users").child("usertype").child(uid)

        val imageUrlHashmap: MutableMap<String, Any> = HashMap()

        imageUrlHashmap["imageUrl"] = "" + profileImageUrl

        ref.updateChildren(imageUrlHashmap)

    }


}
