package com.example.reservation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.reservation.signin.Login
import com.example.reservation.signin.SignUp

//import com.example.reservation.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

//    lateinit var mainBiniding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setContentView(R.layout.activity_main)

    }

    fun funCreateAccount(view: View){
        intent = Intent(this, SignUp::class.java)

        startActivity(intent)
    }
    fun funLoginAccount(view: View){
        intent = Intent(this, Login::class.java)

        startActivity(intent)
    }
}
