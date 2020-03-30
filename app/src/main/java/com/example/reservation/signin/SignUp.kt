package com.example.reservation.signin

import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.reservation.Home
import com.example.reservation.R
import com.example.reservation.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import dmax.dialog.SpotsDialog
import java.util.*
import kotlin.collections.HashMap

class SignUp : AppCompatActivity() {

    val mAuth = FirebaseAuth.getInstance()
    lateinit var mDatabase : DatabaseReference
    lateinit var mBinding : ActivitySignUpBinding
    lateinit var dialog: android.app.AlertDialog


    private var imageUri: Uri? = null
    private val REQUEST_IMAGE_CAPTURE = 100



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up)


        mBinding.userProfilePhoto.setOnClickListener {
            takePictureIntent()
        }

        val registerBtn = mBinding.btnSignup

        dialog = SpotsDialog.Builder().setCancelable(false).setContext(this).build()

        registerBtn.setOnClickListener {
            Log.d("Register","Inside Listener")
            registerUser()
        }

    }

    private fun takePictureIntent() {
        val pictureIntent = Intent(Intent.ACTION_PICK)
        pictureIntent.type = "image/*"
        startActivityForResult(pictureIntent,REQUEST_IMAGE_CAPTURE)

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && data != null) {
             imageUri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver,imageUri)
            val bitmapDrawable = BitmapDrawable(bitmap)

            mBinding.userProfilePhoto.setBackgroundDrawable(bitmapDrawable)

        }
    }



  /*  private fun uploadImageAndSaveUri(bitmap: Bitmap) {
        val baos = ByteArrayOutputStream()
        val storageRef = FirebaseStorage.getInstance()
            .reference
            .child("pics")
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val image = baos.toByteArray()

        val upload = storageRef.putBytes(image)

//        progressbar_pic.visibility = View.VISIBLE
        upload.addOnCompleteListener { uploadTask ->
//            progressbar_pic.visibility = View.INVISIBLE

            if (uploadTask.isSuccessful) {
                storageRef.downloadUrl.addOnCompleteListener { urlTask ->
                    urlTask.result?.let {
                        imageUri = it
                        Toast.makeText(this, imageUri.toString(),Toast.LENGTH_SHORT).show()
                        mBinding.userProfilePhoto.setImageBitmap(bitmap)
                    }
                }
            } else {
                uploadTask.exception?.let {
                    Toast.makeText(this, it.message!!,Toast.LENGTH_SHORT).show()

                }
            }
        }

    }
*/


    private fun registerUser () {

        Log.d("Register","Inside Register Fun")

/*
        val firstnameTxt = mBinding.txtFirstName
        val lastnameTxt = mBinding.txtLastName
        val usernameTxt = mBinding.txtUsername
        val emailTxt = mBinding.txtEmail
        val phonenumberTxt = mBinding.txtPhoneNumber
        val usertypeTxt = mBinding.usertype
        val passwordTxt = mBinding.txtPassword

*/

        var firstname = mBinding.txtFirstName.text.toString()
        var lastname = mBinding.txtLastName.text.toString()
        var username = mBinding.txtUsername.text.toString()
        var email = mBinding.txtEmail.text.toString()
        var phonenumber = mBinding.txtPhoneNumber.text.toString()
        var usertype = mBinding.usertype.text.toString()
        var password = mBinding.txtPassword.text.toString()


        if (firstname.isEmpty()) {
            mBinding.txtFirstName!!.error = "First Name is required"
            mBinding.txtFirstName!!.requestFocus()
            return
        }
        if (lastname.isEmpty()) {
            mBinding.txtLastName!!.error = "Last Name is required"
            mBinding.txtLastName!!.requestFocus()
            return
        }
        if (username.isEmpty()) {
            mBinding.txtUsername!!.error = "Username is required"
            mBinding.txtUsername!!.requestFocus()
            return
        }
        if (email.isEmpty()) {
            mBinding.txtEmail!!.error = "Email is required"
            mBinding.txtEmail!!.requestFocus()
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mBinding.txtEmail!!.error = "Please enter a valid email"
            mBinding.txtEmail!!.requestFocus()
            return
        }
        if (phonenumber.isEmpty()) {
            mBinding.txtPhoneNumber!!.error = "Phone No is required"
            mBinding.txtPhoneNumber!!.requestFocus()
            return
        }
        if (phonenumber.length < 10) {
            mBinding.txtPhoneNumber!!.error = "Minimum length of Phone No should be 10"
            mBinding.txtPhoneNumber!!.requestFocus()
            return
        }
        if (password.isEmpty()) {
            mBinding.txtPassword.error = "Password is required"
            mBinding.txtPassword.requestFocus()
            return
        }
        if (password.length < 6) {
            mBinding.txtPassword.error = "Minimum length of password should be 6"
            mBinding.txtPassword.requestFocus()
            return
        }


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
                    userhashmap["pass"] = "" + password     //just for testing, saving password

                    mDatabase.updateChildren(userhashmap)

                    uploadImageToFirebase()
                    dialog.dismiss()
                    startActivity(Intent(this, Home::class.java))

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
