package com.example.miapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.miapp.navigation.Screen
import com.example.miapp.ui.screens.*
import com.example.miapp.viewmodel.OaViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                val navController = rememberNavController()
                val oaViewModel: OaViewModel = viewModel()
                val listaOas by oaViewModel.listaOas.collectAsState(initial = emptyList())

                NavHost(
                    navController = navController,
                    startDestination = Screen.Login.route
                ) {
                    composable(Screen.Login.route) {
                        LoginScreen(
                            onLoginExitoso = {
                                navController.navigate(Screen.SeleccionPerfil.route) {
                                    popUpTo(Screen.Login.route) { inclusive = true }
                                }
                            }
                        )
                    }

                    composable(Screen.SeleccionPerfil.route) {
                        SeleccionPerfilScreen(
                            onIrAEstudiante = { navController.navigate(Screen.EstudianteHome.route) },
                            onIrADocente = { navController.navigate(Screen.DocenteHome.route) },
                            onIrAUtp = { navController.navigate(Screen.UtpHome.route) }
                        )
                    }

                    composable(Screen.UtpHome.route) {
                        UtpHomeScreen(
                            onCrearOa = { navController.navigate(Screen.FormularioOa.route) },
                            onVerLista = { navController.navigate(Screen.OaLista.route) },
                            onVolver = { navController.popBackStack() }
                        )
                    }

                    composable(Screen.FormularioOa.route) {
                        val context = androidx.compose.ui.platform.LocalContext.current

                        FormularioOaScreen(
                            onGuardarOa = { nuevoOa ->
                                oaViewModel.guardarOa(nuevoOa)
                                enviarNotificacionLocal(
                                    context = context,
                                    titulo = "¡Nueva Actividad Registrada!",
                                    mensaje = "Se ha publicado un nuevo Objetivo de Aprendizaje en la app."
                                )
                                navController.popBackStack()
                            },
                            onVolver = { navController.popBackStack() }
                        )
                    }

                    composable(Screen.DocenteHome.route) {
                        DocenteHomeScreen(
                            onVolver = { navController.popBackStack() },
                            onVerOa = { navController.navigate(Screen.OaLista.route) }
                        )
                    }

                    composable(Screen.EstudianteHome.route) {
                        EstudianteHomeScreen(
                            onVolver = { navController.popBackStack() },
                            onVerOa = { navController.navigate(Screen.OaLista.route) }
                        )
                    }

                    composable(Screen.OaLista.route) {
                        OaListaScreen(
                            listaOas = listaOas,
                            onVolver = { navController.popBackStack() },
                            onRealizarPrueba = { navController.navigate(Screen.PruebaEstudiante.route) }
                        )
                    }

                    composable(Screen.PruebaEstudiante.route) {
                        PruebaEstudianteScreen(
                            onVolver = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}
// Pega esto AL FINAL del archivo MainActivity.kt (fuera de la clase MainActivity)
fun enviarNotificacionLocal(context: android.content.Context, titulo: String, mensaje: String) {
    val notificationManager = context.getSystemService(android.content.Context.NOTIFICATION_SERVICE) as android.app.NotificationManager
    val channelId = "mpaz_canal_alertas"

    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
        val canal = android.app.NotificationChannel(
            channelId,
            "Alertas MPAZ PRO",
            android.app.NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(canal)
    }

    val notificacion = androidx.core.app.NotificationCompat.Builder(context, channelId)
        .setSmallIcon(android.R.drawable.ic_dialog_info)
        .setContentTitle(titulo)
        .setContentText(mensaje)
        .setAutoCancel(true)
        .build()

    notificationManager.notify(101, notificacion)
}