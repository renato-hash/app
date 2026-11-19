package com.example.miapp.navigation

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.miapp.data.OaEntity
import com.example.miapp.enviarNotificacionLocal
import com.example.miapp.ui.screens.*
import com.example.miapp.viewmodel.OaViewModel

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object SeleccionPerfil : Screen("seleccion_perfil")
    object UtpHome : Screen("utp_home")
    object FormularioOa : Screen("formulario_oa")
    object DocenteHome : Screen("docente_home")
    object EstudianteHome : Screen("estudiante_home")
    object OaLista : Screen("oa_lista")
    object PruebaEstudiante : Screen("prueba_estudiante")
}

@Composable
fun AppNavigation() {
    val context = LocalContext.current
    val navController = rememberNavController()
    val oaViewModel: OaViewModel = viewModel()

    val listaOas by oaViewModel.listaOas.collectAsState(initial = emptyList())
    val listaOasPublicados by oaViewModel.listaOasPublicados.collectAsState(initial = emptyList())
    val listaProgreso by oaViewModel.listaProgreso.collectAsState(initial = emptyList())

    var oaSeleccionadoPrueba by remember { mutableStateOf<OaEntity?>(null) }
    var modoLecturaFacil by remember { mutableStateOf(false) }

    val permisoNotifLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { }

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val tienePermiso = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
            if (!tienePermiso) {
                permisoNotifLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

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
                listaOas = listaOas,
                onCrearOa = { navController.navigate(Screen.FormularioOa.route) },
                onAprobarOa = { oa ->
                    oaViewModel.aprobarYPublicarOaUtp(oa)
                    enviarNotificacionLocal(
                        context = context,
                        titulo = "OA Aprobado por UTP",
                        mensaje = "${oa.codigo} - ${oa.titulo} ya está disponible para 2.° Básico."
                    )
                },
                onDeshabilitarOa = { oa ->
                    oaViewModel.deshabilitarOaUtp(oa)
                },
                onVerLista = { navController.navigate(Screen.OaLista.route) },
                onVolver = { navController.popBackStack() }
            )
        }

        composable(Screen.FormularioOa.route) {
            FormularioOaScreen(
                onGuardarOa = { nuevoOa ->
                    oaViewModel.guardarOa(nuevoOa)
                    enviarNotificacionLocal(
                        context = context,
                        titulo = "¡Nuevo OA Registrado!",
                        mensaje = "${nuevoOa.codigo} registrado en el sistema MPAZ PRO."
                    )
                    navController.popBackStack()
                },
                onVolver = { navController.popBackStack() }
            )
        }

        composable(Screen.DocenteHome.route) {
            DocenteHomeScreen(
                oasAutorizados = listaOasPublicados,
                listaProgreso = listaProgreso,
                onGuardarObservacion = { progresoId, obs ->
                    oaViewModel.registrarObservacionDocente(progresoId, obs)
                },
                onVolver = { navController.popBackStack() },
                onVerOa = { navController.navigate(Screen.OaLista.route) }
            )
        }

        composable(Screen.EstudianteHome.route) {
            EstudianteHomeScreen(
                oasPublicados = listaOasPublicados,
                listaProgreso = listaProgreso,
                modoLecturaFacil = modoLecturaFacil,
                onToggleLecturaFacil = { modoLecturaFacil = it },
                onSeleccionarOaParaPrueba = { oa ->
                    oaSeleccionadoPrueba = oa
                    navController.navigate(Screen.PruebaEstudiante.route)
                },
                onVolver = { navController.popBackStack() },
                onVerOa = { navController.navigate(Screen.OaLista.route) }
            )
        }

        composable(Screen.OaLista.route) {
            OaListaScreen(
                listaOas = listaOas,
                onVolver = { navController.popBackStack() },
                onRealizarPrueba = {
                    oaSeleccionadoPrueba = listaOasPublicados.firstOrNull() ?: listaOas.firstOrNull()
                    navController.navigate(Screen.PruebaEstudiante.route)
                }
            )
        }

        composable(Screen.PruebaEstudiante.route) {
            PruebaEstudianteScreen(
                oaActual = oaSeleccionadoPrueba,
                modoLecturaFacil = modoLecturaFacil,
                onGuardarResultado = { oa, puntaje, total, detalle, callback ->
                    oaViewModel.registrarResultadoPrueba(
                        oa = oa,
                        puntajeObtenido = puntaje,
                        puntajeTotal = total,
                        detalleRespuestas = detalle,
                        onResultadoGuardado = callback
                    )
                },
                onVolver = { navController.popBackStack() }
            )
        }
    }
}