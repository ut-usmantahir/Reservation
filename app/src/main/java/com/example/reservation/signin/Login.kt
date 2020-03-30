package com.example.reservation.signin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.reservation.Home
import com.example.reservation.R
import com.example.reservation.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import dmax.dialog.SpotsDialog


class Login : AppCompatActivity() {


    lateinit var mBinding : ActivityLoginBinding
    lateinit var dialog: android.app.AlertDialog
    var mAuth = FirebaseAuth.getInstance()

    lateinit var mDatabase : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        dialog = SpotsDialog.Builder().setCancelable(false).setContext(this).build()

        mBinding.btnSignin.setOnClickListener {
            Log.d("Testing","Inside Listener")
            loginUser()
        }

    }



    private fun loginUser() {

     var email =  mBinding.txtEmail.text.toString().trim()
     var password = mBinding.txtPassword.text.toString().trim()
        if (email == null) {
            mBinding.txtEmail!!.error = "Email is required"
            mBinding.txtEmail!!.requestFocus()
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mBinding.txtEmail!!.error = "Please enter a valid email"
            mBinding.txtEmail!!.requestFocus()
            return
        }
        if (password == null) {
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
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->

                Log.d("Testing","Inside signInWithEmailAndPassword" + email + " and " + password)
                if (task.isSuccessful) {
                    Log.d("Testing","Inside isSuccessful")

                    dialog.dismiss()
                    startActivity(Intent(this, Home::class.java))

                }
                else {
                    dialog.dismiss()
                    Toast.makeText(this, "Error singing in, try again later :(", Toast.LENGTH_LONG).show()
                }
            }
    }

}
