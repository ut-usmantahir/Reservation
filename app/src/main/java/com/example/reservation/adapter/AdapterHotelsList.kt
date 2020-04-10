package com.example.reservation.adapter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.reservation.R
import com.example.reservation.ViewHotelDetails
import com.example.reservation.ViewHotelHandler
import com.example.reservation.databinding.LayoutHotelsListItemBinding
import com.example.reservation.model.Hotel


class AdapterHotelsList(private val context: Context, private val hotelsList: ArrayList<Hotel>): RecyclerView.Adapter<AdapterHotelsList.MyViewHolder>() {

    var listener: ViewHotelHandler? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        return MyViewHolder(
            DataBindingUtil.inflate(LayoutInflater.from(parent.context),
                R.layout.layout_hotels_list_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return hotelsList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

       /* holder.hotelsListBinding.txtHotelName.text = hotelsList[position].hotelName
        holder.hotelsListBinding.txtHotelRooms.text = hotelsList[position].hotelRooms
        holder.hotelsListBinding.txtAvailableRooms.text = hotelsList[position].roomsAvailable
        holder.hotelsListBinding.txtLocation.text = hotelsList[position].hotelLocation
        holder.hotelsListBinding.txtDescription.text = hotelsList[position].hotelDescription*/


        holder.hotelsListBinding.hotel = hotelsList[position]


        holder.hotelsListBinding.root.setOnClickListener {
            Log.d("Usman","setOnClickListener: ")
            Log.d("Usman","setOnClickListener: Path ${hotelsList[position]}")

            listener?.onItemClicked(hotelsList[position])
            val intent = Intent(context, ViewHotelDetails::class.java)
            intent.putExtra("hotelName", hotelsList[position].hotelName)
            intent.putExtra("hotelRooms", hotelsList[position].hotelRooms)
            intent.putExtra("availableRooms", hotelsList[position].roomsAvailable)
            intent.putExtra("location", hotelsList[position].hotelLocation)
            intent.putExtra("description", hotelsList[position].hotelDescription)
            context.startActivity(intent)



            Toast.makeText(context,"Item Clicked",Toast.LENGTH_SHORT).show()

            Log.d("Usman","setOnClickListener: Adapter ")

        }



    }


    inner class MyViewHolder(
        val hotelsListBinding: LayoutHotelsListItemBinding
    ) : RecyclerView.ViewHolder(hotelsListBinding.root)


}
