package com.example.miapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.miapp.data.OaEntity
import com.example.miapp.viewmodel.OaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioOaScreen(
    onGuardarOa: (OaEntity) -> Unit,
    onVolver: () -> Unit,
    oaViewModel: OaViewModel = viewModel()
) {
    // 1. Campos requeridos (String, Int, Date, Checkbox, Switch)
    var codigo by remember { mutableStateOf("") }
    var titulo by remember { mutableStateOf("") }
    var intentosStr by remember { mutableStateOf("3") } // Int
    var fecha by remember { mutableStateOf("2026-11-20") } // Date
    var requiereAudio by remember { mutableStateOf(true) } // Checkbox
    var publicadoInmediato by remember { mutableStateOf(true) } // Switch
    var descripcion by remember { mutableStateOf("") }

    val asignaturas = listOf(
        "Lenguaje y Comunicación",
        "Matemáticas",
        "Ciencias Naturales",
        "Historia y Geografía"
    )
    var asignaturaSeleccionada by remember { mutableStateOf(asignaturas[0]) }
    var expAsignatura by remember { mutableStateOf(false) }

    var errorMensaje by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Administración UTP - Asignar Actividad",
            style = MaterialTheme.typography.headlineSmall
        )

        if (errorMensaje.isNotEmpty()) {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = errorMensaje,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    modifier = Modifier.padding(12.dp)
                )
            }
        }


        OutlinedTextField(
            value = codigo,
            onValueChange = { codigo = it; errorMensaje = "" },
            label = { Text("Código OA (String)") },
            isError = errorMensaje.contains("código", ignoreCase = true),
            trailingIcon = {
                if (errorMensaje.contains("código", ignoreCase = true)) {
                    Icon(Icons.Default.Warning, contentDescription = "Error", tint = Color.Red)
                }
            },
            modifier = Modifier.fillMaxWidth()
        )


        OutlinedTextField(
            value = titulo,
            onValueChange = { titulo = it; errorMensaje = "" },
            label = { Text("Título de la Actividad (String)") },
            isError = errorMensaje.contains("título", ignoreCase = true),
            trailingIcon = {
                if (errorMensaje.contains("título", ignoreCase = true)) {
                    Icon(Icons.Default.Warning, contentDescription = "Error", tint = Color.Red)
                }
            },
            modifier = Modifier.fillMaxWidth()
        )


        OutlinedTextField(
            value = intentosStr,
            onValueChange = { intentosStr = it; errorMensaje = "" },
            label = { Text("Límite de Intentos (Int)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            isError = errorMensaje.contains("intentos", ignoreCase = true),
            modifier = Modifier.fillMaxWidth()
        )


        OutlinedTextField(
            value = fecha,
            onValueChange = { fecha = it; errorMensaje = "" },
            label = { Text("Fecha de Disponibilidad (Date: YYYY-MM-DD)") },
            isError = errorMensaje.contains("fecha", ignoreCase = true),
            modifier = Modifier.fillMaxWidth()
        )


        ExposedDropdownMenuBox(
            expanded = expAsignatura,
            onExpandedChange = { expAsignatura = !expAsignatura }
        ) {
            OutlinedTextField(
                value = asignaturaSeleccionada,
                onValueChange = {},
                readOnly = true,
                label = { Text("Asignatura") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expAsignatura) },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = expAsignatura,
                onDismissRequest = { expAsignatura = false }
            ) {
                asignaturas.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(item) },
                        onClick = {
                            asignaturaSeleccionada = item
                            expAsignatura = false
                        }
                    )
                }
            }
        }


        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Checkbox(
                checked = requiereAudio,
                onCheckedChange = { requiereAudio = it }
            )
            Text(text = "Incluir lectura por voz (Audio para 2° Básico)")
        }


        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Publicar actividad inmediatamente")
            Switch(
                checked = publicadoInmediato,
                onCheckedChange = { publicadoInmediato = it }
            )
        }

        OutlinedTextField(
            value = descripcion,
            onValueChange = { descripcion = it },
            label = { Text("Descripción / Instrucciones") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 2
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            OutlinedButton(onClick = onVolver) {
                Text("Volver")
            }

            Button(onClick = {
                oaViewModel.validarYGuardarOa(
                    codigo = codigo,
                    titulo = titulo,
                    intentosStr = intentosStr,
                    fecha = fecha,
                    requiereAudio = requiereAudio,
                    publicadoInmediato = publicadoInmediato,
                    asignatura = asignaturaSeleccionada,
                    descripcion = descripcion,
                    onExito = { nuevoOa ->
                        onGuardarOa(nuevoOa)
                    },
                    onError = { mensajeError ->
                        errorMensaje = mensajeError
                    }
                )
            }) {
                Text("Guardar / Publicar")
            }
        }
    }
}