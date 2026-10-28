package com.example.miapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {
    var usuario by mutableStateOf("")
        private set

    var clave by mutableStateOf("")
        private set

    var recordarSesion by mutableStateOf(false)
        private set

    var errorUsuario by mutableStateOf<String?>(null)
        private set

    var errorClave by mutableStateOf<String?>(null)
        private set

    fun onUsuarioChange(nuevoUsuario: String) {
        usuario = nuevoUsuario
        if (errorUsuario != null) errorUsuario = null
    }

    fun onClaveChange(nuevaClave: String) {
        clave = nuevaClave
        if (errorClave != null) errorClave = null
    }

    fun onRecordarChange(recordar: Boolean) {
        recordarSesion = recordar
    }

    fun validarEIngresar(onLoginExitoso: () -> Unit) {
        var esValido = true

        if (usuario.isBlank()) {
            errorUsuario = "Ingrese su usuario o RUT"
            esValido = false
        }

        if (clave.length < 4) {
            errorClave = "La contraseña debe tener al menos 4 caracteres"
            esValido = false
        }

        if (esValido) {
            onLoginExitoso()
        }
    }
}