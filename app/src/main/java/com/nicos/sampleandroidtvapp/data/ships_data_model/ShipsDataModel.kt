package com.nicos.sampleandroidtvapp.data.ships_data_model

import com.nicos.sampleandroidtvapp.data.ships_inner_list_data_model.ShipsInnerListDataModel
import com.nicos.sampleandroidtvapp.room_database.ships.ShipsModel

data class ShipsDataModel(var shipsInnerListDataModelList: MutableList<ShipsInnerListDataModel>) {

    companion object {
        fun createShipsDataModel(shipsModelList: MutableList<ShipsModel>): MutableList<ShipsDataModel> {
            return mutableListOf<ShipsDataModel>().apply {
                (0 until 10).forEach { _ ->
                    val shipsInnerListDataModelList = mutableListOf<ShipsInnerListDataModel>()
                    shipsModelList.forEachIndexed { index, it ->
                        shipsInnerListDataModelList.add(
                            ShipsInnerListDataModel(
                                shipsModel = it,
                                isSelect = index == 0
                            )
                        )
                    }
                    add(ShipsDataModel(shipsInnerListDataModelList = shipsInnerListDataModelList))
                }
            }
        }
    }
}
