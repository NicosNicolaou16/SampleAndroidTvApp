package com.nicos.sampleandroidtvapp.compose.ships_screen

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nicos.sampleandroidtvapp.data.ships_data_model.ShipsDataModel
import com.nicos.sampleandroidtvapp.data.repositories.ships_repository.ShipsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShipsViewModel @Inject constructor() : ViewModel() {

    var shipsDataModelStateFlow = MutableStateFlow<MutableList<ShipsDataModel>>(mutableListOf())
    val loading = MutableLiveData<Boolean>()
    val error = MutableLiveData<String>()

    init {
        requestForShipsData()
        offline()
    }

    @Inject
    protected lateinit var shipsRepository: ShipsRepository

    private fun requestForShipsData() = viewModelScope.launch(Dispatchers.Main) {
        loading.value = true
        flow {
            val shipsList =
                shipsRepository.fetchShipsData() //get the data from the server
            val shipsDataModel = ShipsDataModel.createShipsDataModel(shipsList)
            emit(shipsDataModel)
        }.flowOn(Dispatchers.IO)
            .catch { e ->
                loading.value = false
                error.value = e.message.toString()
            }.collect {
                loading.value = false
                shipsDataModelStateFlow.value = it
            }
    }

    private fun offline() = viewModelScope.launch(Dispatchers.Main) {
        loading.value = true
        flow {
            val shipsList =
                shipsRepository.queryToGetAllShips() //get the data from the local database
            emit(ShipsDataModel.createShipsDataModel(shipsList))
        }.flowOn(Dispatchers.IO)
            .catch { e ->
                loading.value = false
                error.value = e.message.toString()
            }.collect {
                loading.value = false
                shipsDataModelStateFlow.value = it
            }
    }
}