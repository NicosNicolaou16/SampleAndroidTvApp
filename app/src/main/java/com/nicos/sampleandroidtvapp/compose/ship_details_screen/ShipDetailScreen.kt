package com.nicos.sampleandroidtvapp.compose.ship_details_screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import coil.size.Scale
import com.nicos.sampleandroidtvapp.R
import com.nicos.sampleandroidtvapp.room_database.ships.ShipsModel
import com.nicos.sampleandroidtvapp.compose.generic_compose_views.ShowDialog
import com.nicos.sampleandroidtvapp.compose.generic_compose_views.StartDefaultLoader
import com.nicos.sampleandroidtvapp.utils.extensions.getProgressDrawable
import kotlinx.coroutines.Dispatchers

const val SHIP_ID_KEY = "ship_id_key"

@Composable
internal fun ShipDetailsScreen(
    shipId: String,
    shipDetailsViewModel: ShipDetailsViewModel = hiltViewModel()
) {
    shipDetailsViewModel.queryShipById(shipId)
    val shipData = shipDetailsViewModel.shipDetails.collectAsState().value
    val snackBarHostState = remember { SnackbarHostState() }
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        },
        content = { paddingValue ->
            val isLoading = shipDetailsViewModel.loading.observeAsState(initial = false).value
            if (isLoading) StartDefaultLoader()
            val error = shipDetailsViewModel.error.observeAsState(initial = null).value
            if (error != null) ShowDialog(title = error, message = "")
            ShipDetailsView(shipData, paddingValue)
        })
}

@Composable
private fun ShipDetailsView(
    shipData: ShipsModel,
    paddingValues: PaddingValues
) {
    val context = LocalContext.current
    Box(contentAlignment = Alignment.BottomCenter) {
        AsyncImage(
            model = ImageRequest.Builder(context = context).apply {
                data(shipData.image)
                scale(Scale.FILL)
                placeholder(getProgressDrawable(context))
                error(R.drawable.ic_baseline_image_24)
                fallback(R.drawable.ic_baseline_image_24)
                memoryCachePolicy(CachePolicy.ENABLED)
                dispatcher(Dispatchers.Default)
            }.build(),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .fillMaxSize()
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(bottom = 50.dp)
        ) {
            Text(
                text = shipData.ship_name ?: "",
                style = TextStyle(
                    fontSize = 25.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.ExtraBold
                ),
                color = Color.White,
            )
            Text(
                text = shipData.ship_type ?: "",
                style = TextStyle(
                    fontSize = 25.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.ExtraBold
                ),
                color = Color.White,
            )
        }
    }
}