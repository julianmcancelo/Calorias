package com.calorias.ai.ui

import android.util.Size
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.calorias.ai.feature.scanner.BarcodeAnalyzer
import com.calorias.ai.feature.scanner.ScannerViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.material3.CircularProgressIndicator

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ScannerScreen(onBack: () -> Unit, viewModel: ScannerViewModel = hiltViewModel()) {
    val cameraPermission = rememberPermissionState(android.Manifest.permission.CAMERA)
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var lastBarcode by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        if (!cameraPermission.status.isGranted) {
            cameraPermission.launchPermissionRequest()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(12.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        if (cameraPermission.status.isGranted) {
            AndroidView(
                modifier = Modifier.weight(1f),
                factory = { ctx ->
                    val previewView = PreviewView(ctx).apply {
                        scaleType = PreviewView.ScaleType.FILL_CENTER
                    }

                    val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
                    cameraProviderFuture.addListener({
                        val cameraProvider = cameraProviderFuture.get()

                        val preview = Preview.Builder()
                            .build()
                            .also { it.setSurfaceProvider(previewView.surfaceProvider) }

                        val analysis = ImageAnalysis.Builder()
                            .setTargetResolution(Size(1280, 720))
                            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                            .build()
                            .also {
                                it.setAnalyzer(
                                    ContextCompat.getMainExecutor(ctx),
                                    BarcodeAnalyzer { barcode ->
                                        val raw = barcode.rawValue ?: ""
                                        if (raw.isNotEmpty() && raw != lastBarcode) {
                                            lastBarcode = raw
                                            viewModel.onBarcodeDetected(raw)
                                        }
                                    }
                                )
                            }

                        try {
                            cameraProvider.unbindAll()
                            cameraProvider.bindToLifecycle(
                                lifecycleOwner,
                                CameraSelector.DEFAULT_BACK_CAMERA,
                                preview,
                                analysis
                            )
                        } catch (_: Exception) {}
                    }, ContextCompat.getMainExecutor(ctx))

                    previewView
                }
            )
        } else {
            Column(
                modifier = Modifier.fillMaxSize().padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Se requiere permiso de cámara")
                Spacer(Modifier.height(12.dp))
                Button(onClick = { cameraPermission.launchPermissionRequest() }) {
                    Text("Conceder permiso")
                }
            }
        }

        val ui = viewModel.state.collectAsState()
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = if (lastBarcode.isBlank()) "Apunta a un código..." else "Detectado: $lastBarcode",
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(Modifier.height(8.dp))
            if (ui.value.loading) {
                CircularProgressIndicator()
            } else {
                ui.value.error?.let { Text("No encontrado", color = MaterialTheme.colorScheme.error) }
                ui.value.productName?.let { name ->
                    Text(name, style = MaterialTheme.typography.titleMedium)
                    val kcal100 = ui.value.kcalPer100g?.let { it.toInt() }
                    val kcalServ = ui.value.kcalPerServing?.let { it.toInt() }
                    val size = ui.value.servingSize
                    Spacer(Modifier.height(4.dp))
                    if (kcal100 != null) Text("kcal/100g: $kcal100")
                    if (kcalServ != null) Text("kcal/porción: $kcalServ")
                    if (!size.isNullOrBlank()) Text("porción: $size")
                }
            }
            Spacer(Modifier.height(8.dp))
            Button(onClick = onBack) { Text("Volver") }
        }
    }
}
