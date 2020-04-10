package com.example.reservation

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.reservation.adapter.AdapterHotelsList
import com.example.reservation.databinding.ActivityHotelsListBinding
import com.example.reservation.model.Hotel
import com.google.firebase.database.*
import dmax.dialog.SpotsDialog

class HotelsList : AppCompatActivity() {

    lateinit var layoutManager: LinearLayoutManager
    lateinit var hotelsListAdapter: AdapterHotelsList
    lateinit var mHotelReference: DatabaseReference
    var hotelList : ArrayList<Hotel> ? = null
    lateinit var hotelBinding : ActivityHotelsListBinding
    lateinit var dialog: android.app.AlertDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        hotelBinding = DataBindingUtil.setContentView(this, R.layout.activity_hotels_list)

        dialog = SpotsDialog.Builder().setCancelable(false).setContext(this).build()

        layoutManager = LinearLayoutManager(this)
        hotelBinding.rvHotelsList.layoutManager = layoutManager
        hotelBinding.rvHotelsList.setHasFixedSize(true)




        hotelList = arrayListOf<Hotel>()
        mHotelReference = FirebaseDatabase.getInstance().getReference("hotels")


        mHotelReference?.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                if(p0!!.exists()){
                    Log.d("HotelsList", p0.getValue().toString())

                    dialog.show()


                    for (h in p0.children){

                        val hotel = h.getValue(Hotel::class.java)
                        Log.d("HotelsList", hotel.toString())

                        hotelList?.add(hotel!!)
                    }

                    Log.d("HotelsList","hotelList after "+ hotelList.toString())

                    hotelsListAdapter =
                        AdapterHotelsList(
                            this@HotelsList,
                            hotelList!!
                        )
                    hotelBinding.rvHotelsList.adapter = hotelsListAdapter

                    dialog.dismiss()

                }
                else{
                    dialog.dismiss()
                    Toast.makeText(this@HotelsList,"No Hotels found:(",Toast.LENGTH_SHORT).show()
                }

            }
        })
        dialog.dismiss()



    }

}
