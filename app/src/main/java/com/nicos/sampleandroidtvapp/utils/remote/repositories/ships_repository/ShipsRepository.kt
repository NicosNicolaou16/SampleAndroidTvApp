package com.nicos.sampleandroidtvapp.utils.remote.repositories.ships_repository

import com.nicos.sampleandroidtvapp.room_database.init_database.MyRoomDatabase
import com.nicos.sampleandroidtvapp.room_database.ships.ShipsModel
import com.nicos.sampleandroidtvapp.utils.remote.ship_service.ShipService
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class ShipsRepository @Inject constructor(
    private var shipService: ShipService,
    var myRoomDatabase: MyRoomDatabase
) {

    suspend fun fetchShipsData(): MutableList<ShipsModel> {
        val shipsList = shipService.getShips()
        saveShipDataIntoDatabase(shipsList)
        return shipsList
    }

    private suspend fun saveShipDataIntoDatabase(shipsModelList: MutableList<ShipsModel>) {
        ShipsModel.insertTheShips(shipsModelList, myRoomDatabase).collect()
    }

    suspend fun queryToGetAllShips(): MutableList<ShipsModel> {
        return myRoomDatabase.shipDao().getAllShips()
    }
}