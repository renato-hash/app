package com.example.miapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.miapp.data.OaEntity
import com.example.miapp.data.ProgresoEntity
import com.example.miapp.model.ProgresoRemote
import com.example.miapp.ui.components.SemaforoProgresoCard
import com.example.miapp.viewmodel.ProgresoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DocenteHomeScreen(
    oasAutorizados: List<OaEntity>,
    listaProgreso: List<ProgresoEntity>,
    onGuardarObservacion: (Int, String) -> Unit,
    onVolver: () -> Unit,
    onVerOa: () -> Unit,
    progresoViewModel: ProgresoViewModel = viewModel()
) {
    val progresosRemotos by progresoViewModel.progresosList.collectAsState()
    val mensajeRemoto by progresoViewModel.mensajeEstado.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Módulo Docente - 2.° Básico") }) }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Panel Pedagógico Reservado",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "OAs autorizados por UTP: ${oasAutorizados.size} | Evaluaciones locales: ${listaProgreso.size} | Servidor: ${progresosRemotos.size}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        mensajeRemoto?.let { msg ->
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = msg,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = onVerOa,
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Consultar OAs")
                            }
                            OutlinedButton(
                                onClick = { progresoViewModel.cargarProgresos() },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Sincronizar API")
                            }
                        }
                    }
                }
            }

            item {
                SemaforoProgresoCard(
                    evaluacionesCompletadas = listaProgreso.size,
                    puntajeObtenidoTotal = listaProgreso.sumOf { it.puntajeObtenido },
                    puntajeMaximoTotal = listaProgreso.sumOf { it.puntajeTotal }
                )
            }

            if (progresosRemotos.isNotEmpty()) {
                item {
                    Text(
                        text = "Registros Sincronizados en Microservicio (MySQL)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                items(progresosRemotos, key = { it.id ?: it.hashCode() }) { remoto ->
                    var obsRemota by remember(remoto.id) {
                        mutableStateOf(
                            if (remoto.observacionDocente == "Sin observaciones registradas") ""
                            else remoto.observacionDocente
                        )
                    }

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = "${remoto.estudianteFicticio} • ${remoto.codigoOa}: ${remoto.tituloOa}",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Puntaje: ${remoto.puntajeObtenido}/${remoto.puntajeTotal} (${remoto.porcentajeAvance}%)",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = obsRemota,
                                onValueChange = { obsRemota = it },
                                label = { Text("Observación remota") },
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                onClick = {
                                    remoto.id?.let { idLong ->
                                        val actualizado: ProgresoRemote = remoto.copy(
                                            observacionDocente = obsRemota.ifBlank { "Validado por docente" },
                                            validadoPorDocente = true
                                        )
                                        progresoViewModel.actualizarProgreso(idLong, actualizado)
                                    }
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(Icons.Default.Done, contentDescription = null)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Validar en Servidor (PUT)")
                            }
                        }
                    }
                }
            }

            item {
                Text(
                    text = "Informes de Progreso Locales y Observaciones",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            if (listaProgreso.isEmpty()) {
                item {
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "Aún no hay evaluaciones rendidas por estudiantes. Cuando un estudiante rinda una prueba, aquí aparecerá su desglose para tu validación pedagógica.",
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            } else {
                items(listaProgreso) { informe ->
                    var observacionInput by remember(informe.id) {
                        mutableStateOf(
                            if (informe.observacionDocente == "Sin observaciones registradas") ""
                            else informe.observacionDocente
                        )
                    }

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "${informe.estudianteFicticio} • Intento #${informe.intentoNumero}",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${informe.codigoOa}: ${informe.tituloOa}",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Puntaje: ${informe.puntajeObtenido}/${informe.puntajeTotal} (${informe.porcentajeAvance}%)",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Desglose: ${informe.detalleRespuestas}",
                                style = MaterialTheme.typography.bodySmall
                            )
                            Text(
                                text = "Fecha/Hora: ${informe.marcaTiempo}",
                                style = MaterialTheme.typography.bodySmall
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            OutlinedTextField(
                                value = observacionInput,
                                onValueChange = { observacionInput = it },
                                label = { Text("Observación pedagógica del docente") },
                                placeholder = { Text("Ej: Reforzar comprensión lectora...") },
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Button(
                                onClick = { onGuardarObservacion(informe.id, observacionInput) },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(Icons.Default.Done, contentDescription = null)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    if (informe.validadoPorDocente) "Informe Validado (Actualizar)"
                                    else "Validar y Guardar Observación"
                                )
                            }
                        }
                    }
                }
            }

            item {
                OutlinedButton(
                    onClick = onVolver,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Volver a Selección de Perfil")
                }
            }
        }
    }
}