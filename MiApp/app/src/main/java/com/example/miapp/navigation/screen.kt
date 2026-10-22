package com.example.miapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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
            FormularioOaScreen(
                onGuardarOa = { nuevoOa ->
                    oaViewModel.guardarOa(nuevoOa)
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