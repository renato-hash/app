package com.example.miapp.ui.screens

import android.Manifest
import android.annotation.SuppressLint
import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.android.gms.location.LocationServices

@SuppressLint("MissingPermission")
@Composable
fun PruebaEstudianteScreen(
    onVolver: () -> Unit
) {
    val context = LocalContext.current

    // Estado para la Cámara
    var fotoBitmap by remember { mutableStateOf<Bitmap?>(null) }
    val tomadorFoto = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null) {
            fotoBitmap = bitmap
        }
    }

    // Estado para el GPS
    var ubicacionTexto by remember { mutableStateOf("Ubicación no registrada") }
    val clienteUbicacion = remember { LocationServices.getFusedLocationProviderClient(context) }

    val solicitarPermisoGps = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { estaConcedido ->
        if (estaConcedido) {
            clienteUbicacion.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    ubicacionTexto = "Lat: ${location.latitude.toString().take(7)}, Lon: ${location.longitude.toString().take(7)}"
                } else {
                    ubicacionTexto = "GPS activo (Buscando señal...)"
                }
            }
        } else {
            ubicacionTexto = "Permiso de ubicación denegado"
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Prueba de Diagnóstico - 2° Básico",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Card 1: Verificación de GPS
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "1. Registro de Asistencia Escola (GPS)",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Estado: $ubicacionTexto", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = { solicitarPermisoGps.launch(Manifest.permission.ACCESS_FINE_LOCATION) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Obtener GPS del Colegio")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Card 2: Evidencia con Cámara
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "2. Evidencia del Estudiante (Cámara)",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = { tomadorFoto.launch(null) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Tomar Foto")
                }

                fotoBitmap?.let { bitmap ->
                    Spacer(modifier = Modifier.height(12.dp))
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = "Foto tomada",
                        modifier = Modifier
                            .size(160.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Botón Volver
        OutlinedButton(
            onClick = onVolver,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Volver")
        }
    }
}