package com.example.miapp.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLoginExitoso: () -> Unit
) {
    var usuario by remember { mutableStateOf("") }
    var clave by remember { mutableStateOf("") }
    var recordarSesion by remember { mutableStateOf(false) }

    var errorUsuario by remember { mutableStateOf<String?>(null) }
    var errorClave by remember { mutableStateOf<String?>(null) }
    var loginCorrecto by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Acceso MPAZ PRO 2.º") }) }
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
                text = "Iniciar Sesión",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(20.dp))

            // Campo Usuario
            OutlinedTextField(
                value = usuario,
                onValueChange = { usuario = it },
                label = { Text("Nombre de Usuario / RUT") },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                isError = errorUsuario != null,
                trailingIcon = {
                    if (errorUsuario != null) Icon(Icons.Default.Warning, contentDescription = null, tint = MaterialTheme.colorScheme.error)
                },
                modifier = Modifier.fillMaxWidth()
            )
            errorUsuario?.let {
                Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Campo Contraseña
            OutlinedTextField(
                value = clave,
                onValueChange = { clave = it },
                label = { Text("Contraseña") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                visualTransformation = PasswordVisualTransformation(),
                isError = errorClave != null,
                trailingIcon = {
                    if (errorClave != null) Icon(Icons.Default.Warning, contentDescription = null, tint = MaterialTheme.colorScheme.error)
                },
                modifier = Modifier.fillMaxWidth()
            )
            errorClave?.let {
                Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Checkbox(
                    checked = recordarSesion,
                    onCheckedChange = { recordarSesion = it }
                )
                Text("Recordar mi perfil en este dispositivo")
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    var valido = true
                    if (usuario.isBlank()) {
                        errorUsuario = "Ingrese su usuario o RUT"
                        valido = false
                    } else {
                        errorUsuario = null
                    }

                    if (clave.length < 4) {
                        errorClave = "La contraseña debe tener al menos 4 caracteres"
                        valido = false
                    } else {
                        errorClave = null
                    }

                    if (valido) {
                        loginCorrecto = true
                        onLoginExitoso()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ingresar al Sistema")
            }

            AnimatedVisibility(visible = loginCorrecto) {
                Card(
                    modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Text(
                        "¡Sesión iniciada correctamente!",
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }
        }
    }
}