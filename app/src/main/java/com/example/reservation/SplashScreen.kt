package com.example.reservation

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class SplashScreen : AppCompatActivity() {

    var handler: Handler? = null
    lateinit var mRef : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

         var mAuth = FirebaseAuth.getInstance()



        handler = Handler()
        handler!!.postDelayed(Runnable {


            if (mAuth.currentUser != null){

                Log.d("SplashScreen","mAuth: ${mAuth.currentUser!!.uid.toString()}")

                 mRef = FirebaseDatabase.getInstance().reference.child("users").child(""+ mAuth.currentUser!!.uid.toString())

                mRef.addListenerForSingleValueEvent(object :
                    ValueEventListener {
                    override fun onDataChange(@NonNull dataSnapshot: DataSnapshot) {
                        val map = dataSnapshot.value as Map<String, Any>?
                        if (map!!["usertype"].toString().contains("Customer")) {
//                            dialog.dismiss()
                            startActivity(Intent(this@SplashScreen, SignUpCustomer::class.java))
                            finish()

                        } else {
//                            dialog.dismiss()
                            startActivity(Intent(this@SplashScreen, HomeOwner::class.java))
                            finish()
                        }
                    }

                    override fun onCancelled(@NonNull databaseError: DatabaseError) {}
                })

            } else {
                Log.d("SplashScreen","mAuth: NULL")

                val i = Intent(this@SplashScreen, MainActivity::class.java)
//                i.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(i)
                finish()
            }

        }, 1000)
    }
}




