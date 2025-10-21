package com.calorias.ai.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.calorias.ai.health.HealthViewModel
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(onScanClick: () -> Unit, healthViewModel: HealthViewModel = hiltViewModel()) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Calorías AI", style = MaterialTheme.typography.headlineMedium)
        Text(
            text = "Escaneá un código de barras o QR para registrar alimentos",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 8.dp, bottom = 24.dp)
        )
        Button(onClick = onScanClick) {
            Text("Abrir escáner")
        }

        val ui = healthViewModel.state.collectAsState()
        val scope = rememberCoroutineScope()
        val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) {
            healthViewModel.refresh()
        }

        Text(
            text = when {
                !ui.value.available -> "Health Connect no disponible"
                ui.value.hasPermissions -> "Health Connect: permisos concedidos"
                else -> "Health Connect: sin permisos"
            },
            modifier = Modifier.padding(top = 24.dp)
        )

        if (ui.value.available && !ui.value.hasPermissions) {
            Button(
                onClick = {
                    scope.launch {
                        val req = healthViewModel.buildPermissionRequest()
                        launcher.launch(req)
                    }
                },
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text("Conectar Health Connect")
            }
        }
    }
}
