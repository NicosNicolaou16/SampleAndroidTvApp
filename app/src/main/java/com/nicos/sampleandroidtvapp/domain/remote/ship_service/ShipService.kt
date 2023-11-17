package com.nicos.sampleandroidtvapp.domain.remote.ship_service

import com.nicos.sampleandroidtvapp.room_database.ships.ShipsModel
import retrofit2.http.GET

interface ShipService {

    @GET("v3/ships")
    suspend fun getShips(): MutableList<ShipsModel>
}