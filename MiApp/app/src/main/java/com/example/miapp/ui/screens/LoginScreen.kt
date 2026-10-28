package com.example.miapp.ui.screens

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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.miapp.viewmodel.LoginViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLoginExitoso: () -> Unit,
    loginViewModel: LoginViewModel = viewModel()
) {
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

            OutlinedTextField(
                value = loginViewModel.usuario,
                onValueChange = { loginViewModel.onUsuarioChange(it) },
                label = { Text("Nombre de Usuario / RUT") },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                isError = loginViewModel.errorUsuario != null,
                trailingIcon = {
                    if (loginViewModel.errorUsuario != null) {
                        Icon(Icons.Default.Warning, contentDescription = null, tint = MaterialTheme.colorScheme.error)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
            loginViewModel.errorUsuario?.let {
                Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }

            Spacer(modifier = Modifier.height(12.dp))


            OutlinedTextField(
                value = loginViewModel.clave,
                onValueChange = { loginViewModel.onClaveChange(it) },
                label = { Text("Contraseña") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                visualTransformation = PasswordVisualTransformation(),
                isError = loginViewModel.errorClave != null,
                trailingIcon = {
                    if (loginViewModel.errorClave != null) {
                        Icon(Icons.Default.Warning, contentDescription = null, tint = MaterialTheme.colorScheme.error)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
            loginViewModel.errorClave?.let {
                Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Checkbox(
                    checked = loginViewModel.recordarSesion,
                    onCheckedChange = { loginViewModel.onRecordarChange(it) }
                )
                Text("Recordar mi perfil en este dispositivo")
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    loginViewModel.validarEIngresar(onLoginExitoso)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ingresar al Sistema")
            }
        }
    }
}