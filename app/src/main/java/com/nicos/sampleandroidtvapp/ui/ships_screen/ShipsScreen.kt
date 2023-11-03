package com.nicos.sampleandroidtvapp.ui.ships_screen

import android.widget.Toast
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.tv.foundation.lazy.list.TvLazyColumn
import androidx.tv.foundation.lazy.list.TvLazyRow
import androidx.tv.foundation.lazy.list.items
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.ImmersiveList
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import coil.size.Scale
import com.nicos.sampleandroidtvapp.R
import com.nicos.sampleandroidtvapp.data.ships_inner_list_data_model.ShipsInnerListDataModel
import com.nicos.sampleandroidtvapp.ui.generic_compose_views.CustomToolbar
import com.nicos.sampleandroidtvapp.ui.generic_compose_views.ShowDialog
import com.nicos.sampleandroidtvapp.ui.generic_compose_views.StartDefaultLoader
import com.nicos.sampleandroidtvapp.utils.extensions.getProgressDrawable
import com.nicos.sampleandroidtvapp.utils.screen_routes.Screens.SHIP_DETAILS_SCREEN
import kotlinx.coroutines.Dispatchers

@Composable
internal fun ShipsScreen(navController: NavController) {
    val scaffoldState: ScaffoldState = rememberScaffoldState()
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            CustomToolbar(R.string.list_of_ships)
        },
        content = { paddingValue ->
            ListOfShips(navController = navController, paddingValues = paddingValue)
        })
}

@Composable
private fun ListOfShips(
    navController: NavController,
    paddingValues: PaddingValues,
    shipsViewModel: ShipsViewModel = hiltViewModel()
) {
    val isLoading = shipsViewModel.loading.observeAsState(initial = false).value
    if (isLoading) StartDefaultLoader()
    val error = shipsViewModel.error.observeAsState(initial = "").value
    if (error != null && error.isNotEmpty()) ShowDialog(
        title = stringResource(id = R.string.error),
        message = error
    )
    val context = LocalContext.current
    val shipsDataModelList =
        shipsViewModel.shipsDataModelStateFlow.collectAsState(initial = mutableListOf()).value
    TvLazyColumn {
        items(shipsDataModelList) {
            ShipItemView(shipsInnerListDataModelList = it.shipsInnerListDataModelList) { selectedShipDataValue ->
                Toast.makeText(
                    context,
                    selectedShipDataValue.shipsModel.ship_name.toString(),
                    Toast.LENGTH_SHORT
                ).show()
                navController.navigate(SHIP_DETAILS_SCREEN + "/${selectedShipDataValue.shipsModel.ship_id}")
            }
        }
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
private fun ShipItemView(
    shipsInnerListDataModelList: MutableList<ShipsInnerListDataModel>,
    listener: (ShipsInnerListDataModel) -> Unit
) {
    ImmersiveList(
        modifier = Modifier
            .height(325.dp)
            .fillMaxWidth()
            .padding(5.dp),
        background = { index, _ ->
            AnimatedContent(
                targetState = index,
                transitionSpec = {
                    fadeIn(animationSpec = tween(durationMillis = 500)) togetherWith
                            fadeOut(animationSpec = tween(durationMillis = 500))
                },
            ) {
                RowItemList(shipsInnerListDataModelList = shipsInnerListDataModelList, listener = listener)
            }
        },
    ){}
}

@Composable
fun RowItemList(
    shipsInnerListDataModelList: MutableList<ShipsInnerListDataModel>,
    listener: (ShipsInnerListDataModel) -> Unit
) {
    val context = LocalContext.current
    TvLazyRow {
        items(shipsInnerListDataModelList) { shipsInnerListDataModel ->
            Card(
                modifier = Modifier
                    .height(425.dp)
                    .width(425.dp)
                    .padding(15.dp)
                    .clickable {
                        listener(shipsInnerListDataModel)
                    },
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(context = context).apply {
                            data(shipsInnerListDataModel.shipsModel.image)
                            scale(Scale.FIT)
                            placeholder(getProgressDrawable(context))
                            error(R.drawable.ic_baseline_image_24)
                            fallback(R.drawable.ic_baseline_image_24)
                            memoryCachePolicy(CachePolicy.ENABLED)
                            dispatcher(Dispatchers.Default)
                        }.build(),
                        contentDescription = null,
                        contentScale = ContentScale.FillBounds,
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(width = 280.dp)
                    )
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .wrapContentWidth()
                            .fillMaxHeight()
                            .padding(start = 15.dp),
                    ) {
                        Column {
                            Text(
                                shipsInnerListDataModel.shipsModel.ship_name.toString(),
                                style = TextStyle(
                                    fontSize = 15.sp,
                                    textAlign = TextAlign.Center
                                ),
                                color = Color.White,
                            )
                            Text(
                                shipsInnerListDataModel.shipsModel.ship_type.toString(),
                                style = TextStyle(
                                    fontSize = 15.sp,
                                    textAlign = TextAlign.Center
                                ),
                                color = Color.White,
                            )
                        }
                    }
                }
            }
        }
    }
}