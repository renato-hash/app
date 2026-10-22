package com.example.miapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.miapp.data.OaEntity

@Composable
fun OaListaScreen(
    listaOas: List<OaEntity>,
    onVolver: () -> Unit,
    onRealizarPrueba: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Lista de Objetivos de Aprendizaje",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (listaOas.isEmpty()) {
            Text("No hay OAs registrados todavía.")
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(listaOas) { oa ->
                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "${oa.codigo} - ${oa.titulo}",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(text = "Asignatura: ${oa.asignatura}")
                            Text(text = "Estado: ${oa.estado} | Recurso: ${oa.tipoRecurso}")
                            Text(text = "Autoría: ${oa.autoria}")
                            Text(text = "Descripción: ${oa.descripcion}")
                            if (oa.fechaModificacion.isNotEmpty()) {
                                Text(
                                    text = "Fecha: ${oa.fechaModificacion}",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onRealizarPrueba,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        ) {
            Text("Rendir Prueba Evaluativa")
        }

        OutlinedButton(
            onClick = onVolver,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Volver")
        }
    }
}