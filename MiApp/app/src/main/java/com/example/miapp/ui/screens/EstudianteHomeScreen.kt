package com.example.miapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EstudianteHomeScreen(
    onVolver: () -> Unit,
    onVerOa: () -> Unit
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Módulo Estudiante - 2.° Básico") }) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "¡Hola, Estudiante!",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onVerOa,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ver Mis OAs y Evaluaciones 📚")
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = onVolver,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Volver a Selección de Perfil")
            }
        }
    }
}