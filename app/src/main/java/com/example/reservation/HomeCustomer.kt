package com.example.reservation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.reservation.databinding.ActivityHomeCustomerBinding

class HomeCustomer : AppCompatActivity() {

    lateinit var homeCustomerBinding : ActivityHomeCustomerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        homeCustomerBinding = DataBindingUtil.setContentView(this, R.layout.activity_home_customer)

    }
}
