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
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import dmax.dialog.SpotsDialog
import java.util.*
import kotlin.collections.HashMap

class SignUp : AppCompatActivity()  {



    private lateinit var mDatabase : DatabaseReference


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
        if(imageUri.toString().isEmpty()){
            Toast.makeText(this,"Please select a profile photo",Toast.LENGTH_SHORT).show()
            return
        }


//        val intent: Intent

        if(userType=="Customer"){
//             intent = Intent(this, SignUpCustomer::class.java)
            registerCustomer()
        }
        else{
          val intent = Intent(this, SignUpOwner::class.java)
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
//            finish()

        }


    }

    private fun registerCustomer(){

        Log.d("Register","SignUp, Inside On registerCustomer")

      /*  val bundle = intent.extras

        var firstname = bundle?.get("firstname").toString()
        var lastname = bundle?.get("lastname").toString()
        var username = bundle?.get("username").toString()
        var email = bundle?.get("email").toString()
        var phonenumber = bundle?.get("phonenumber").toString()
        var userType = bundle?.get("userType").toString()
        var password = bundle?.get("password").toString()

//      Uri.parse(extras.getString("KEY"));

        imageUri = Uri.parse(bundle?.get("imageUri") as String?)*/

        var firstname = mBinding.txtFirstName.text.toString()
        var lastname = mBinding.txtLastName.text.toString()
        var username = mBinding.txtUsername.text.toString()
        var email = mBinding.txtEmail.text.toString()
        var phonenumber = mBinding.txtPhoneNumber.text.toString()
        var password = mBinding.txtPassword.text.toString()



        dialog.show()
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                Log.d("Register", "Inside of createUserWithEmailAndPassword")

                val user = task.result?.user
                val uid = user!!.uid

                mDatabase = FirebaseDatabase.getInstance().reference.child("users").child(uid)

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
                startActivity(Intent(this, SignUpCustomer::class.java))
                finish()
//              On map ready function call here

                Toast.makeText(this, "Account Created Successfully :)", Toast.LENGTH_LONG).show()
            }else {
                dialog.dismiss()
                Toast.makeText(this, "Error registering, try again later :(", Toast.LENGTH_LONG).show()
//                startActivity(Intent(this, SignUp::class.java))

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

        val ref = FirebaseDatabase.getInstance().reference.child("users").child(uid)

        val imageUrlHashmap: MutableMap<String, Any> = HashMap()

        imageUrlHashmap["imageUrl"] = "" + profileImageUrl

        ref.updateChildren(imageUrlHashmap)

    }




}
