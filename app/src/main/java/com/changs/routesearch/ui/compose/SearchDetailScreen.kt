package com.changs.routesearch.ui.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.changs.routesearch.ui.MapViewModel
import com.changs.routesearch.util.formatMonthDay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchDetailScreen(
    mapViewModel: MapViewModel, type: String, onBackClick: () -> Unit
) {
    val searchUiState by mapViewModel.searchUiState.collectAsState()

    LaunchedEffect(key1 = searchUiState.searchText) {
        mapViewModel.searchRegions()
    }

    val keyboardController = LocalSoftwareKeyboardController.current

    Scaffold(topBar = {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    "검색",
                )
            },
            navigationIcon = {
                IconButton(onClick = {
                    mapViewModel.updateSearchText(TextFieldValue(""))
                    onBackClick()
                }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Localized description"
                    )
                }
            },
        )
    }, content = { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {

                OutlinedTextField(value = searchUiState.searchText,
                    onValueChange = {
                        mapViewModel.updateSearchText(it)
                    },
                    label = { Text("장소, 주소 검색") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Search
                    ),
                    keyboardActions = KeyboardActions(onSearch = {
                        mapViewModel.addRecentSearch()
                        keyboardController?.hide()
                    })
                )

                if (searchUiState.searchText.text.isEmpty()) {
                    Text(
                        text = "최근 검색", modifier = Modifier.padding(vertical = 8.dp)
                    )

                    LazyColumn {
                        items(searchUiState.recentSearchs) { search ->
                            Row(modifier = Modifier
                                .clickable {
                                    mapViewModel.updateSearchText(TextFieldValue(search.name))
                                }
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically) {
                                Text(text = search.name, fontSize = 18.sp, color = Color.Black)
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = formatMonthDay(search.timestamp),
                                        fontSize = 14.sp,
                                        color = Color.Gray
                                    )

                                    IconButton(onClick = {
                                        mapViewModel.deleteRecentSearch(search.name)
                                    }) {
                                        Icon(
                                            imageVector = Icons.Filled.Clear,
                                            contentDescription = "Delete"
                                        )
                                    }
                                }
                            }
                        }
                    }
                } else {
                    Text(
                        text = "검색 결과", modifier = Modifier.padding(vertical = 8.dp)
                    )

                    LazyColumn {
                        items(searchUiState.regionSearchs) { result ->
                            Column(modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .clickable {
                                    if (type == "departures") {
                                        mapViewModel.updateDepartureLocation(result)
                                    } else {
                                        mapViewModel.updateDestinationLocation(result)
                                    }
                                    mapViewModel.addRecentSearch()
                                    mapViewModel.updateSearchText(TextFieldValue(""))
                                    onBackClick()
                                }) {
                                Text(text = result.name, fontSize = 18.sp, color = Color.Black)
                            }
                        }
                    }
                }
            }
        }
    })

}
