package com.changs.routesearch.ui.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class SearchResult(val name: String, val address: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchDetailScreen(
    onBackClick: () -> Unit,
    onSearch: (String) -> Unit,
    recentSearches: List<String>,
    searchResults: List<SearchResult>
) {
    var searchQuery by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        TopAppBar(
            title = { Text(text = "검색") },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = searchQuery,
            onValueChange = {
                searchQuery = it
                onSearch(it)
            },
            label = { Text("Search...") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        )

        Text(
            text = "Recent Searches",
            modifier = Modifier.padding(vertical = 8.dp)
        )

        LazyColumn {
            items(recentSearches) { search ->
                Text(
                    text = search,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { searchQuery = search; onSearch(search) }
                        .padding(vertical = 8.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Search Results",
            modifier = Modifier.padding(vertical = 8.dp)
        )

        LazyColumn {
            items(searchResults) { result ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Text(text = result.name, fontSize = 18.sp, color = Color.Black)
                    Text(text = result.address, fontSize = 14.sp, color = Color.Gray)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SearchDetailScreenPreview() {
    val recentSearches = listOf("Search 1", "Search 2", "Search 3", "Search 4", "Search 5")
    val searchResults = listOf(
        SearchResult(name = "Place 1", address = "Address 1"),
        SearchResult(name = "Place 2", address = "Address 2"),
        SearchResult(name = "Place 3", address = "Address 3"),
        SearchResult(name = "Place 4", address = "Address 4"),
        SearchResult(name = "Place 5", address = "Address 5"),
        SearchResult(name = "Place 6", address = "Address 6"),
        SearchResult(name = "Place 7", address = "Address 7"),
        SearchResult(name = "Place 8", address = "Address 8"),
        SearchResult(name = "Place 9", address = "Address 9"),
        SearchResult(name = "Place 10", address = "Address 10")
    )

    SearchDetailScreen(
        onBackClick = {},
        onSearch = {},
        recentSearches = recentSearches,
        searchResults = searchResults
    )
}
