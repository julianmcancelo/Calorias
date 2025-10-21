package com.calorias.ai.feature.scanner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.calorias.ai.data.repository.FoodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class ScannerViewModel @Inject constructor(
    private val foodRepository: FoodRepository
) : ViewModel() {

    data class UiState(
        val loading: Boolean = false,
        val lastBarcode: String = "",
        val productName: String? = null,
        val kcalPer100g: Double? = null,
        val kcalPerServing: Double? = null,
        val servingSize: String? = null,
        val error: String? = null
    )

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state

    private var lookupJob: Job? = null
    private var lastQueried: String = ""

    fun onBarcodeDetected(code: String) {
        if (code.isBlank() || code == lastQueried) {
            _state.value = _state.value.copy(lastBarcode = code)
            return
        }
        lastQueried = code
        lookup(code)
    }

    private fun lookup(code: String) {
        lookupJob?.cancel()
        lookupJob = viewModelScope.launch {
            _state.value = _state.value.copy(loading = true, lastBarcode = code, error = null)
            // pequeña espera para evitar spam si el analizador emite rápido
            delay(120)
            val product = foodRepository.getProductByBarcode(code)
            if (product == null) {
                _state.value = _state.value.copy(loading = false, productName = null, kcalPer100g = null, kcalPerServing = null, servingSize = null, error = "No encontrado")
            } else {
                _state.value = _state.value.copy(
                    loading = false,
                    productName = product.productName ?: product.brands,
                    kcalPer100g = product.nutriments?.energyKcal100g,
                    kcalPerServing = product.nutriments?.energyKcalServing,
                    servingSize = product.nutriments?.servingSize,
                    error = null
                )
            }
        }
    }
}
