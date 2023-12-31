package com.nicos.sampleandroidtvapp.data.repositories.ship_details_repository

import com.nicos.sampleandroidtvapp.room_database.init_database.MyRoomDatabase
import com.nicos.sampleandroidtvapp.room_database.ships.ShipsModel
import javax.inject.Inject

class ShipDetailsRepository  @Inject constructor(
    private var myRoomDatabase: MyRoomDatabase
) {

    suspend fun queryShipById(id: String): ShipsModel? {
        return myRoomDatabase.shipDao().getShipById(id)
    }
}