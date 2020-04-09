package com.example.reservation

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.reservation.databinding.ActivityHomeOwnerBinding
import com.google.firebase.auth.FirebaseAuth

class HomeOwner : AppCompatActivity() {

    lateinit var homeOwnerBinding : ActivityHomeOwnerBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeOwnerBinding = DataBindingUtil.setContentView(this, R.layout.activity_home_owner)

        homeOwnerBinding.btnLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()

            val intent = Intent(this@HomeOwner, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
