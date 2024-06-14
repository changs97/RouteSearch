package com.changs.routesearch.ui.compose

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.changs.routesearch.ui.RouteUiState
import com.changs.routesearch.ui.SearchViewModel

@Composable
fun SearchScreen(
    searchViewModel: SearchViewModel,
    onBackClick: () -> Unit,
    onDeparturesClick: () -> Unit,
    onArrivalsClick: () -> Unit,
    onCompleteClick: () -> Unit
) {
    val lifecycle = LocalLifecycleOwner.current
    val routeUiState by searchViewModel.routeUiState.collectAsStateWithLifecycle(lifecycle)

    SearchScreenContent(
        routeUiState = routeUiState,
        onBackClick = onBackClick,
        onDeparturesClick = onDeparturesClick,
        onArrivalsClick = onArrivalsClick,
        onCompleteClick = onCompleteClick
    )
}

@Composable
fun SearchScreenContent(
    routeUiState: RouteUiState,
    onBackClick: () -> Unit,
    onDeparturesClick: () -> Unit,
    onArrivalsClick: () -> Unit,
    onCompleteClick: () -> Unit
) {
    Scaffold(
        topBar = {
            RouteSearchTopBar(onBackClick = onBackClick, "경로 설정")
        },
        content = { paddingValues ->
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        PlaceSearchButton(
                            text = routeUiState.departureLocation?.name ?: "출발지",
                            onClick = onDeparturesClick
                        )

                        PlaceSearchButton(
                            text = routeUiState.destinationLocation?.name ?: "도착지",
                            onClick = onArrivalsClick
                        )
                    }

                    CompleteButton(
                        isEnabled = routeUiState.departureLocation != null && routeUiState.destinationLocation != null,
                        onCompleteClick = onCompleteClick
                    )
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RouteSearchTopBar(onBackClick: () -> Unit, title: String) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                title,
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
        },
    )
}

@Composable
fun PlaceSearchButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp)
            .clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxSize(),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(text = text, fontSize = 18.sp)
        }
    }
}

@Composable
fun CompleteButton(
    isEnabled: Boolean,
    onCompleteClick: () -> Unit
) {
    Button(
        onClick = onCompleteClick,
        shape = RoundedCornerShape(8.dp),
        enabled = isEnabled,
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
    ) {
        Text(text = "완료", fontSize = 18.sp)
    }
}

@Preview(showBackground = true)
@Composable
fun TopBarPreview() {
    RouteSearchTopBar(onBackClick = {}, "경로 설정")
}

@Preview(showBackground = true)
@Composable
fun PlaceSearchButtonPreview() {
    PlaceSearchButton(text = "출발지", onClick = {})
}

@Preview(showBackground = true)
@Composable
fun CompleteButtonPreview() {
    CompleteButton(isEnabled = true, onCompleteClick = {})
}

@Preview(showBackground = true)
@Composable
fun SearchScreenContentPreview() {
    SearchScreenContent(
        routeUiState = RouteUiState(),
        onBackClick = {},
        onDeparturesClick = {},
        onArrivalsClick = {},
        onCompleteClick = {}
    )
}