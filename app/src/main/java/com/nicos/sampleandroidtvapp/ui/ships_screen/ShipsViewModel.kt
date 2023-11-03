package com.nicos.sampleandroidtvapp.ui.ships_screen

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nicos.sampleandroidtvapp.R
import com.nicos.sampleandroidtvapp.application.SampleAndroidTvApp
import com.nicos.sampleandroidtvapp.data.ships_data_model.ShipsDataModel
import com.nicos.sampleandroidtvapp.utils.remote.repositories.ships_repository.ShipsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShipsViewModel @Inject constructor(application: Application) : AndroidViewModel(application) {

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
                //getApplication<SampleAndroidTvApp>().getString(R.string.something_went_wrong)
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
                error.value =
                    getApplication<SampleAndroidTvApp>().getString(R.string.something_went_wrong)
            }.collect {
                loading.value = false
                shipsDataModelStateFlow.value = it
            }
    }
}