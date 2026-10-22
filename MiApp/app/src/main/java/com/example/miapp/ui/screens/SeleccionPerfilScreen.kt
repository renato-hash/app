package com.example.miapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeleccionPerfilScreen(
    onIrAUtp: () -> Unit,
    onIrADocente: () -> Unit,
    onIrAEstudiante: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("MPAZ PRO 2.° Básico") })
        }
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
                text = "Selecciona tu Perfil",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onIrAEstudiante,
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) {
                Text("Estudiante (2.° Básico)")
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = onIrADocente,
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) {
                Text("Docente")
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = onIrAUtp,
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) {
                Text("Jefatura UTP")
            }
        }
    }
}