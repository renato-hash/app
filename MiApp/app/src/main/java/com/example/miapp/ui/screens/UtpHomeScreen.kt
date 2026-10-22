package com.example.miapp.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.miapp.data.AppDatabase
import com.example.miapp.viewmodel.OaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UtpHomeScreen(onVolver: () -> Unit) {
    val context = LocalContext.current
    val db = remember { AppDatabase.getDatabase(context) }
    val viewModel = remember { OaViewModel(db) }
    val listaOa by viewModel.listaOa.collectAsState()

    var exitoGuardado by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Jefatura UTP - Gestión de OAs") }) }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                Text("Formulario de Registro de OA", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(8.dp))

                // Campo Código
                OutlinedTextField(
                    value = viewModel.codigoInput,
                    onValueChange = { viewModel.codigoInput = it },
                    label = { Text("Código del OA") },
                    isError = viewModel.codigoError != null,
                    trailingIcon = {
                        if (viewModel.codigoError != null) {
                            Icon(Icons.Default.Warning, contentDescription = "Error", tint = MaterialTheme.colorScheme.error)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                viewModel.codigoError?.let { error ->
                    Text(error, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Campo Título
                OutlinedTextField(
                    value = viewModel.tituloInput,
                    onValueChange = { viewModel.tituloInput = it },
                    label = { Text("Título de la Unidad") },
                    isError = viewModel.tituloError != null,
                    trailingIcon = {
                        if (viewModel.tituloError != null) {
                            Icon(Icons.Default.Warning, contentDescription = "Error", tint = MaterialTheme.colorScheme.error)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                viewModel.tituloError?.let { error ->
                    Text(error, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Campo Horas Estimadas
                OutlinedTextField(
                    value = viewModel.horasInput,
                    onValueChange = { viewModel.horasInput = it },
                    label = { Text("Horas Pedagógicas (Ej: 12)") },
                    isError = viewModel.horasError != null,
                    trailingIcon = {
                        if (viewModel.horasError != null) {
                            Icon(Icons.Default.Warning, contentDescription = "Error", tint = MaterialTheme.colorScheme.error)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                viewModel.horasError?.let { error ->
                    Text(error, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Campo Fecha
                OutlinedTextField(
                    value = viewModel.fechaInput,
                    onValueChange = { viewModel.fechaInput = it },
                    label = { Text("Fecha de Publicación (AAAA-MM-DD)") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Controles Switch y Checkbox
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Publicación Activa:")
                    Switch(
                        checked = viewModel.activoInput,
                        onCheckedChange = { viewModel.activoInput = it }
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = viewModel.audioRequeridoInput,
                        onCheckedChange = { viewModel.audioRequeridoInput = it }
                    )
                    Text("Requiere lectura por voz (TextToSpeech)")
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        if (viewModel.validarYGuardar()) {
                            exitoGuardado = true
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Guardar en Base de Datos")
                }

                AnimatedVisibility(visible = exitoGuardado) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                    ) {
                        Text(
                            "¡OA registrado exitosamente en Room DB!",
                            modifier = Modifier.padding(12.dp),
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text("OAs Registrados (${listaOa.size})", style = MaterialTheme.typography.titleMedium)
            }

            items(listaOa) { oa ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("${oa.codigo}: ${oa.titulo}", style = MaterialTheme.typography.titleSmall)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(oa.descripcionEscrita, style = MaterialTheme.typography.bodySmall)
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedButton(onClick = onVolver, modifier = Modifier.fillMaxWidth()) {
                    Text("Volver a Selección de Perfil")
                }
            }
        }
    }
}