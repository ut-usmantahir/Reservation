package com.example.reservation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.reservation.databinding.ActivityHomeOwnerBinding

class HomeOwner : AppCompatActivity() {

    lateinit var homeOwnerBinding : ActivityHomeOwnerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeOwnerBinding = DataBindingUtil.setContentView(this, R.layout.activity_home_owner)

    }
}
