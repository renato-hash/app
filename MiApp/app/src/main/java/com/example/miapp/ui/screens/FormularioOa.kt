package com.example.miapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.miapp.data.OaEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioOaScreen(
    onGuardarOa: (OaEntity) -> Unit,
    onVolver: () -> Unit
) {
    var codigo by remember { mutableStateOf("") }
    var titulo by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }

    // 1. Las 4 asignaturas del nivel exigidas por la rúbrica
    val asignaturas = listOf(
        "Lenguaje y Comunicación",
        "Matemáticas",
        "Ciencias Naturales",
        "Historia y Geografía"
    )
    var asignaturaSeleccionada by remember { mutableStateOf(asignaturas[0]) }
    var expAsignatura by remember { mutableStateOf(false) }

    // 2. Control de estados de administración
    val estados = listOf("Borrador", "Aprobado", "Publicado", "Deshabilitado")
    var estadoSeleccionado by remember { mutableStateOf(estados[0]) }
    var expEstado by remember { mutableStateOf(false) }

    // 3. Tipos de recursos pedagógicos
    val recursos = listOf("Texto", "Audio", "Imagen", "Prueba Final")
    var recursoSeleccionado by remember { mutableStateOf(recursos[0]) }

    // 4. Validación de autoría y licenciamiento
    val autorias = listOf("Contenido Propio", "Dominio Público", "Autorizado / Licenciado")
    var autoriaSeleccionada by remember { mutableStateOf(autorias[0]) }

    var errorMensaje by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Administración UTP - Gestión de OAs",
            style = MaterialTheme.typography.headlineSmall
        )

        if (errorMensaje.isNotEmpty()) {
            Text(
                text = errorMensaje,
                color = Color.Red,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        OutlinedTextField(
            value = codigo,
            onValueChange = { codigo = it },
            label = { Text("Código OA (ej: OA_01)") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = titulo,
            onValueChange = { titulo = it },
            label = { Text("Título / Unidad") },
            modifier = Modifier.fillMaxWidth()
        )

        // Selección de Asignatura (4 materias)
        ExposedDropdownMenuBox(
            expanded = expAsignatura,
            onExpandedChange = { expAsignatura = !expAsignatura }
        ) {
            OutlinedTextField(
                value = asignaturaSeleccionada,
                onValueChange = {},
                readOnly = true,
                label = { Text("Asignatura (4 Niveles)") },
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

        // Selección de Estado (Borrador, Aprobado, Publicado, Deshabilitado)
        ExposedDropdownMenuBox(
            expanded = expEstado,
            onExpandedChange = { expEstado = !expEstado }
        ) {
            OutlinedTextField(
                value = estadoSeleccionado,
                onValueChange = {},
                readOnly = true,
                label = { Text("Estado del Recurso / OA") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expEstado) },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = expEstado,
                onDismissRequest = { expEstado = false }
            ) {
                estados.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(item) },
                        onClick = {
                            estadoSeleccionado = item
                            expEstado = false
                        }
                    )
                }
            }
        }

        OutlinedTextField(
            value = descripcion,
            onValueChange = { descripcion = it },
            label = { Text("Explicación / Contenido") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3
        )

        Text("Tipo de Recurso:", style = MaterialTheme.typography.bodyMedium)
        Row {
            recursos.forEach { rec ->
                Row(modifier = Modifier.padding(end = 8.dp)) {
                    RadioButton(
                        selected = (recursoSeleccionado == rec),
                        onClick = { recursoSeleccionado = rec }
                    )
                    Text(rec, modifier = Modifier.padding(top = 12.dp))
                }
            }
        }

        Text("Licenciamiento / Autoría:", style = MaterialTheme.typography.bodyMedium)
        autorias.forEach { aut ->
            Row {
                RadioButton(
                    selected = (autoriaSeleccionada == aut),
                    onClick = { autoriaSeleccionada = aut }
                )
                Text(aut, modifier = Modifier.padding(top = 12.dp))
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            OutlinedButton(onClick = onVolver) {
                Text("Volver")
            }

            Button(onClick = {
                if (codigo.isBlank() || titulo.isBlank() || descripcion.isBlank()) {
                    errorMensaje = "Todos los campos son obligatorios"
                } else {
                    val fechaActual = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())
                    val nuevoOa = OaEntity(
                        codigo = codigo,
                        titulo = titulo,
                        asignatura = asignaturaSeleccionada,
                        unidad = "Unidad 1",
                        descripcion = descripcion,
                        tipoRecurso = recursoSeleccionado,
                        estado = estadoSeleccionado,
                        autoria = autoriaSeleccionada,
                        fechaModificacion = fechaActual
                    )
                    onGuardarOa(nuevoOa)
                }
            }) {
                Text("Guardar / Publicar")
            }
        }
    }
}