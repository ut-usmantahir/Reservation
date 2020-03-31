package com.example.reservation

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.reservation.databinding.ActivityHomeCustomerBinding
import com.example.reservation.databinding.ActivityHomeOwnerBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class HomeCustomer : AppCompatActivity() {


    lateinit var mBinding : ActivityHomeCustomerBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_home_customer)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_home_customer)



    }


}
