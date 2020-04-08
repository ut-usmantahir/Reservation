package com.example.reservation.signin

import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.reservation.R
import com.example.reservation.SignUpCustomer
import com.example.reservation.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import dmax.dialog.SpotsDialog

class SignUp : AppCompatActivity()  {

    val mAuth = FirebaseAuth.getInstance()
//    lateinit var mDatabase : DatabaseReference
    lateinit var mBinding : ActivitySignUpBinding
    lateinit var dialog: android.app.AlertDialog
    lateinit var userType:String


    private var imageUri: Uri? = null
    private val REQUEST_IMAGE_CAPTURE = 100



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up)

        val userTypeArray = arrayOf("Customer","Owner")
        val arrayAdapter = ArrayAdapter(this,android.R.layout.simple_spinner_item,userTypeArray)

        mBinding.spUsertype.adapter = arrayAdapter

        mBinding.spUsertype?.onItemSelectedListener = object:
                    AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
                    Toast.makeText(this@SignUp,"Please select an item",Toast.LENGTH_SHORT).show()
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                userType = userTypeArray[p2]
                Log.d("Register","Item Selected in Spinner: ${userType}")
            }

        }

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
/*
        if(imageUri == null){
            Toast.makeText(this,"Please select a profile photo",Toast.LENGTH_SHORT).show()
            return
        }*/


        val intent: Intent

        if(userType=="Customer"){
             intent = Intent(this, SignUpCustomer::class.java)
        }
        else{
            intent = Intent(this, SignUpOwner::class.java)
        }

        intent.putExtra("firstname",firstname)
        intent.putExtra("lastname",lastname)
        intent.putExtra("username",username)
        intent.putExtra("email",email)
        intent.putExtra("phonenumber",phonenumber)
        intent.putExtra("userType",userType)
        intent.putExtra("password",password)
        intent.putExtra("imageUri",imageUri.toString())

        Log.d("Register",firstname)

        startActivity(intent)

    }



}
