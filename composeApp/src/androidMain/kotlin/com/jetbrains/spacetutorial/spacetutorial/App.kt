package com.jetbrains.spacetutorial.spacetutorial

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jetbrains.spacetutorial.theme.AppTheme
import com.jetbrains.spacetutorial.theme.appThemeSuccessful
import com.jetbrains.spacetutorial.theme.appThemeUnsuccessful
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    val viewModel = koinViewModel<RocketLaunchViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val coroutineScope = rememberCoroutineScope()
    var isRefreshing by remember { mutableStateOf(false) }
    val pullToRefreshState = rememberPullToRefreshState()

    AppTheme {
        Scaffold(
            topBar = { AppTopAppBar() }
        ) { paddingValues ->
            PullToRefreshBox(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                state = pullToRefreshState,
                isRefreshing = isRefreshing,
                onRefresh = {
                    isRefreshing = true
                    coroutineScope.launch {
                        viewModel.loadLaunches()
                        isRefreshing = false
                    }
                }
            ) {
                if (state.isLoading && !isRefreshing) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            text = "Loading...",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                } else {
                    LazyColumn {
                        items(state.launches) { launch ->
                            Column(
                                modifier = Modifier
                                    .padding(16.dp)
                            ) {
                                Text(
                                    text = "${launch.missionName} - ${launch.launchYear}",
                                    style = MaterialTheme.typography.headlineSmall
                                )
                                Spacer(Modifier.height(8.dp))
                                Text(
                                    text = if (launch.launchSuccess == true) "Successful" else "Unsuccessful",
                                    color = if (launch.launchSuccess == true) appThemeSuccessful else appThemeUnsuccessful
                                )
                                Spacer(Modifier.height(8.dp))
                                val details = launch.details
                                if (details != null && details.isNotBlank()) {
                                    Text(details)
                                }
                            }
                            HorizontalDivider()
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppTopAppBar(
    modifier: Modifier = Modifier
) {
    TopAppBar(
        modifier = modifier,
        title = {
            Text(
                text = "SpaceX Launches",
                style = MaterialTheme.typography.headlineLarge
            )
        }
    )
}


@Preview
@Composable
private fun AppPreview() {
    AppTheme {
        App()
    }
}