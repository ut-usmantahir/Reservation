package com.example.reservation

import com.example.reservation.model.Hotel

interface ViewHotelHandler {
    fun onItemClicked(hotel: Hotel)
}