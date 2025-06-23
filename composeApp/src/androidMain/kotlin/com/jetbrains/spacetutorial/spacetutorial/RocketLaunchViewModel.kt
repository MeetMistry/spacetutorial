package com.jetbrains.spacetutorial.spacetutorial

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jetbrains.spacetutorial.spacetutorial.entity.RocketLaunch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RocketLaunchViewModel(private val sdk: SpaceXSDK) : ViewModel() {
    private val _state = MutableStateFlow(RocketLaunchScreenState())
    val state = _state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = RocketLaunchScreenState()
    )

    init {
        loadLaunches()
    }

    fun loadLaunches() {
        viewModelScope.launch {
            updateProgress(true)
            try {
                val launches = sdk.getLaunches(forceReload = true)
                _state.update {
                    it.copy(isLoading = false, launches = launches)
                }
            } catch (e: Exception) {
                println("Error: ${e.localizedMessage}")
                updateProgress(false)
            }
        }
    }

    private fun updateProgress(isLoading: Boolean) {
        _state.update {
            it.copy(isLoading = isLoading)
        }
    }
}

data class RocketLaunchScreenState(
    val isLoading: Boolean = false,
    val launches: List<RocketLaunch> = emptyList()
)