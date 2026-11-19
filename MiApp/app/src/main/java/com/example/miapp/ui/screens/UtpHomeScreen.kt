package com.example.miapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.miapp.data.OaEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UtpHomeScreen(
    listaOas: List<OaEntity>,
    onCrearOa: () -> Unit,
    onAprobarOa: (OaEntity) -> Unit,
    onDeshabilitarOa: (OaEntity) -> Unit,
    onVerLista: () -> Unit,
    onVolver: () -> Unit
) {
    val pendientes = listaOas.count { it.estado != "Publicado" }
    val publicados = listaOas.count { it.estado == "Publicado" }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Jefatura UTP - Administración") })
        }
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
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Control Curricular 2.° Básico",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Publicados: $publicados  |  Pendientes/Inactivos: $pendientes",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = onCrearOa,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Nuevo OA")
                    }
                    OutlinedButton(
                        onClick = onVerLista,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Vista General")
                    }
                }
            }

            item {
                Text(
                    text = "Revisión y Autorización de OAs",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            if (listaOas.isEmpty()) {
                item {
                    Text(
                        text = "No hay Objetivos de Aprendizaje registrados aún.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            } else {
                items(listaOas) { oa ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${oa.codigo} - ${oa.asignatura}",
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                AssistChip(
                                    onClick = {},
                                    label = { Text(oa.estado) }
                                )
                            }

                            Text(
                                text = oa.titulo,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Autoría: ${oa.autoria} | Intentos máx.: ${oa.intentosMaximos}",
                                style = MaterialTheme.typography.bodySmall
                            )
                            Text(
                                text = "Trazabilidad: ${oa.trazabilidadUtp}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Button(
                                    onClick = { onAprobarOa(oa) },
                                    enabled = oa.estado != "Publicado",
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Publicar")
                                }

                                OutlinedButton(
                                    onClick = { onDeshabilitarOa(oa) },
                                    enabled = oa.estado == "Publicado",
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(Icons.Default.Close, contentDescription = null, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Deshabilitar")
                                }
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedButton(
                    onClick = onVolver,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Cerrar Sesión / Volver")
                }
            }
        }
    }
}