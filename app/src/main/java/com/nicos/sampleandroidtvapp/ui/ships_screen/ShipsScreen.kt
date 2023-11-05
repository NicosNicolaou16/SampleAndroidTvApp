package com.nicos.sampleandroidtvapp.ui.ships_screen

import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.tv.foundation.PivotOffsets
import androidx.tv.foundation.lazy.list.TvLazyColumn
import androidx.tv.foundation.lazy.list.TvLazyRow
import androidx.tv.foundation.lazy.list.itemsIndexed
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
        backgroundColor = Color.Black,
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
    var isSelectIndexes by rememberSaveable { mutableStateOf(Pair(0, 0)) }
    val focusRequesters = remember { mutableMapOf<Pair<Int, Int>, FocusRequester>() }
    TvLazyColumn {
        itemsIndexed(shipsDataModelList) { indexColumn, it ->
            RowItemList(
                shipsInnerListDataModelList = it.shipsInnerListDataModelList,
                indexColumn = indexColumn,
                isSelectIndexes = isSelectIndexes,
                focusRequesters = focusRequesters,
                selectedItemListener = { selectedShipDataValue ->
                    Toast.makeText(
                        context,
                        selectedShipDataValue.shipsModel.ship_name.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                    navController.navigate(SHIP_DETAILS_SCREEN + "/${selectedShipDataValue.shipsModel.ship_id}")
                }, focusListener = { column, row ->
                    isSelectIndexes = Pair(column, row)
                })
        }
    }
}

@Composable
fun RowItemList(
    shipsInnerListDataModelList: MutableList<ShipsInnerListDataModel>,
    indexColumn: Int,
    isSelectIndexes: Pair<Int, Int>,
    focusRequesters: MutableMap<Pair<Int, Int>, FocusRequester>,
    focusListener: (Int, Int) -> Unit,
    selectedItemListener: (ShipsInnerListDataModel) -> Unit
) {
    TvLazyRow(
        pivotOffsets = PivotOffsets(0.5f, 0.5f),
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        itemsIndexed(shipsInnerListDataModelList) { index, shipsInnerListDataModel ->
            ShipItemCard(
                shipsInnerListDataModel = shipsInnerListDataModel,
                indexRow = index,
                indexColumn = indexColumn,
                isSelectIndexes = isSelectIndexes,
                focusRequesters = focusRequesters,
                focusListener = focusListener,
                selectedItemListener = selectedItemListener
            )
        }
    }
}

@Composable
fun ShipItemCard(
    shipsInnerListDataModel: ShipsInnerListDataModel,
    indexColumn: Int,
    indexRow: Int,
    isSelectIndexes: Pair<Int, Int>,
    focusRequesters: MutableMap<Pair<Int, Int>, FocusRequester>,
    focusListener: (Int, Int) -> Unit,
    selectedItemListener: (ShipsInnerListDataModel) -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    focusRequesters[Pair(indexColumn, indexRow)] = focusRequester
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .height(325.dp)
            .width(325.dp)
            .padding(top = 20.dp)
            .focusRequester(focusRequester)
            .onFocusChanged {
                if (it.hasFocus) focusListener(indexColumn, indexRow)

            }
            .focusable()
            .selectable(isSelected(isSelectIndexes, indexColumn, indexRow)) {
                selectedItemListener(shipsInnerListDataModel)
            }
            .border(
                if (isSelected(isSelectIndexes, indexColumn, indexRow)) 7.dp else 0.dp,
                if (isSelected(
                        isSelectIndexes,
                        indexColumn,
                        indexRow
                    )
                ) Color.White else Color.Transparent,
                shape = RoundedCornerShape(7.dp)
            )
    ) {
        Box(
            contentAlignment = Alignment.BottomCenter,
            modifier = Modifier
                .wrapContentWidth()
                .fillMaxHeight()
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
                modifier = Modifier.fillMaxSize()
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(bottom = 50.dp)
            ) {
                Text(
                    shipsInnerListDataModel.shipsModel.ship_name.toString(),
                    style = TextStyle(
                        fontSize = 25.sp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.ExtraBold
                    ),
                    color = Color.Green,
                )
                Text(
                    shipsInnerListDataModel.shipsModel.ship_type.toString(),
                    style = TextStyle(
                        fontSize = 25.sp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.ExtraBold
                    ),
                    color = Color.Green,
                )
            }
        }
    }
    LaunchedEffect(Unit) {
        focusRequesters[isSelectIndexes]?.requestFocus()
    }
}

fun isSelected(isSelectIndex: Pair<Int, Int>, indexColumn: Int, indexRow: Int) =
    isSelectIndex.first == indexColumn && isSelectIndex.second == indexRow