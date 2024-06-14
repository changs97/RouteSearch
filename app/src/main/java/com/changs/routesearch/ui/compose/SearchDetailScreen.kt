package com.changs.routesearch.ui.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.changs.routesearch.data.model.PoiInfo
import com.changs.routesearch.data.model.RecentSearch
import com.changs.routesearch.ui.SearchUiState
import com.changs.routesearch.ui.SearchViewModel
import com.changs.routesearch.util.formatMonthDay

@Composable
fun SearchDetailScreen(
    searchViewModel: SearchViewModel, type: String, onBackClick: () -> Unit
) {
    val lifecycle = LocalLifecycleOwner.current
    val searchUiState by searchViewModel.searchUiState.collectAsStateWithLifecycle(lifecycle)

    LaunchedEffect(key1 = searchUiState.searchText) {
        searchViewModel.searchRegions()
    }

    DisposableEffect(Unit) {
        onDispose {
            searchViewModel.updateSearchText(TextFieldValue(""))
        }
    }

    SearchDetailScreenContent(searchUiState = searchUiState,
        type = type,
        onBackClick = onBackClick,
        onSearchTextChanged = { searchViewModel.updateSearchText(it) },
        onSearchExecuted = {
            searchViewModel.addRecentSearch()
        },
        onRecentSearchSelected = { searchViewModel.updateSearchText(TextFieldValue(it.name)) },
        onDeleteRecentSearch = { searchViewModel.deleteRecentSearch(it.name) },
        onResultSelected = {
            if (type == "departures") {
                searchViewModel.updateDepartureLocation(it)
            } else {
                searchViewModel.updateDestinationLocation(it)
            }
            searchViewModel.addRecentSearch()
            onBackClick()
        })
}

@Composable
fun SearchDetailScreenContent(
    searchUiState: SearchUiState,
    type: String,
    onBackClick: () -> Unit,
    onSearchTextChanged: (TextFieldValue) -> Unit,
    onSearchExecuted: () -> Unit,
    onRecentSearchSelected: (RecentSearch) -> Unit,
    onDeleteRecentSearch: (RecentSearch) -> Unit,
    onResultSelected: (PoiInfo) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Scaffold(topBar = {
        RouteSearchTopBar(onBackClick = onBackClick, "검색")
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

                SearchInputField(searchText = searchUiState.searchText,
                    onSearchTextChanged = onSearchTextChanged,
                    onSearchExecuted = {
                        onSearchExecuted()
                        keyboardController?.hide()
                    })

                if (searchUiState.searchText.text.isEmpty()) {
                    RecentSearchesList(
                        recentSearches = searchUiState.recentSearchs,
                        onRecentSearchSelected = onRecentSearchSelected,
                        onDeleteRecentSearch = onDeleteRecentSearch
                    )
                } else {
                    SearchResultsList(
                        searchResults = searchUiState.regionSearchs,
                        onResultSelected = onResultSelected
                    )
                }
            }
        }
    })
}

@Composable
fun SearchInputField(
    searchText: TextFieldValue,
    onSearchTextChanged: (TextFieldValue) -> Unit,
    onSearchExecuted: () -> Unit
) {
    OutlinedTextField(
        value = searchText,
        onValueChange = onSearchTextChanged,
        label = { Text("장소, 주소 검색") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        singleLine = true,
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(onSearch = {
            onSearchExecuted()
        })
    )
}

@Composable
fun RecentSearchesList(
    recentSearches: List<RecentSearch>,
    onRecentSearchSelected: (RecentSearch) -> Unit,
    onDeleteRecentSearch: (RecentSearch) -> Unit
) {
    Text(
        text = "최근 검색",
        fontSize = 18.sp,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.padding(vertical = 8.dp)
    )

    LazyColumn {
        items(recentSearches) { search ->
            Row(modifier = Modifier
                .clickable { onRecentSearchSelected(search) }
                .fillMaxWidth()
                .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically) {
                Text(text = search.name, fontSize = 15.sp)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = formatMonthDay(search.timestamp),
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                    IconButton(onClick = { onDeleteRecentSearch(search) }) {
                        Icon(
                            modifier = Modifier.size(18.dp),
                            imageVector = Icons.Filled.Clear,
                            contentDescription = "Delete"
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SearchResultsList(
    searchResults: List<PoiInfo>, onResultSelected: (PoiInfo) -> Unit
) {
    Text(
        text = "검색 결과",
        fontSize = 18.sp,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.padding(vertical = 8.dp)
    )

    LazyColumn {
        items(searchResults) { result ->
            Column(modifier = Modifier
                .clickable { onResultSelected(result) }
                .fillMaxWidth()
                .padding(vertical = 4.dp)) {
                Row(verticalAlignment = Alignment.Bottom) {
                    Icon(
                        imageVector = Icons.Filled.LocationOn, contentDescription = "Location"
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(text = result.name, fontSize = 15.sp)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SearchDetailScreenContentPreview() {
    val dummyUiState = SearchUiState(
        searchText = TextFieldValue(""), recentSearchs = emptyList(), regionSearchs = emptyList()
    )

    SearchDetailScreenContent(searchUiState = dummyUiState,
        type = "departures",
        onBackClick = {},
        onSearchTextChanged = {},
        onSearchExecuted = {},
        onRecentSearchSelected = {},
        onDeleteRecentSearch = {},
        onResultSelected = {})
}
