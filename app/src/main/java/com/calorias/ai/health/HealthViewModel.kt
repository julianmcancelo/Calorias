package com.calorias.ai.health

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HealthViewModel @Inject constructor(
    private val repo: HealthConnectRepository
) : ViewModel() {

    data class UiState(
        val available: Boolean = false,
        val hasPermissions: Boolean = false,
        val loading: Boolean = false,
        val error: String? = null
    )

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state

    init {
        refresh()
    }

    fun refresh() {
        val available = repo.isAvailable()
        _state.value = _state.value.copy(available = available)
        if (!available) return
        viewModelScope.launch {
            val has = repo.hasAllPermissions()
            _state.value = _state.value.copy(hasPermissions = has)
        }
    }

    suspend fun buildPermissionRequest() = repo.createPermissionsRequest()

    fun permissions() = repo.permissions()
}
