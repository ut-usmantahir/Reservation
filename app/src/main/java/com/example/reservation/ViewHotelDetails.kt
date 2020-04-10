package com.example.reservation

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import com.example.reservation.databinding.ActivityViewHotelDetailsBinding
import com.example.reservation.model.Hotel

class ViewHotelDetails : AppCompatActivity(), ViewHotelHandler{

    lateinit var hotelBinding : ActivityViewHotelDetailsBinding
     var hotelData: Hotel?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hotelBinding = DataBindingUtil.setContentView(this, R.layout.activity_view_hotel_details)

        Log.d("ViewHotelDetails","ViewHotelDetails, onCreate()")


        getData()


    }


    private fun getData(){

        val bundle:Bundle? = intent.extras

        val hotelName = bundle?.get("hotelName").toString()
        val hotelRooms = bundle?.get("hotelRooms").toString()
        val availableRooms = bundle?.get("availableRooms").toString()
        val location = bundle?.get("location").toString()
        val description = bundle?.get("description").toString()


        Log.d("ViewHotelDetails","hotel ${hotelName.toString()}")

        hotelBinding.hotel = Hotel(hotelName,hotelRooms,availableRooms,location,description)
        Log.d("ViewHotelDetails","${hotelBinding.hotel.toString()}")


    }

    override fun onItemClicked(hotel: Hotel) {
        hotelData = hotel

        hotelBinding.hotel = hotelData
        Log.d("ViewHotelDetails","onItemClicked ${hotel.toString()}")


    }


}
