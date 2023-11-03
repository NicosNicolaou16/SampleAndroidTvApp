package com.nicos.sampleandroidtvapp.ui.ship_details_screen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nicos.sampleandroidtvapp.R
import com.nicos.sampleandroidtvapp.application.SampleAndroidTvApp
import com.nicos.sampleandroidtvapp.room_database.init_database.MyRoomDatabase
import com.nicos.sampleandroidtvapp.room_database.ships.ShipsModel
import com.nicos.sampleandroidtvapp.utils.remote.repositories.ship_details_repository.ShipDetailsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShipDetailsViewModel @Inject constructor(application: Application) :
    AndroidViewModel(application) {

    val shipDetails = MutableStateFlow<ShipsModel>(ShipsModel())
    internal val loading = MutableLiveData<Boolean>()
    internal val error = MutableLiveData<String>()

    @Inject
    protected lateinit var shipDetailsRepository: ShipDetailsRepository

    fun queryShipById(id: String) = viewModelScope.launch(Dispatchers.Main) {
        flow {
            val shipsModel: ShipsModel? = shipDetailsRepository.queryShipById(id)
            emit(shipsModel)
        }.flowOn(Dispatchers.Default)
            .catch { e ->
                error.value = getApplication<SampleAndroidTvApp>().getString(R.string.something_went_wrong)
            }.collect {
                if (it != null) {
                    shipDetails.value = it
                }
            }
    }
}