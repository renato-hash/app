package com.example.miapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.miapp.ui.screens.*

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object SeleccionPerfil : Screen("seleccion_perfil")
    object UtpHome : Screen("utp_home")
    object DocenteHome : Screen("docente_home")
    object EstudianteHome : Screen("estudiante_home")
    object OaLista : Screen("oa_lista")
    object PruebaEstudiante : Screen("prueba_estudiante")
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        // 1. Pantalla de Autenticación
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginExitoso = {
                    navController.navigate(Screen.SeleccionPerfil.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        // 2. Selección de Perfil
        composable(Screen.SeleccionPerfil.route) {
            SeleccionPerfilScreen(
                onIrAEstudiante = { navController.navigate(Screen.EstudianteHome.route) },
                onIrADocente = { navController.navigate(Screen.DocenteHome.route) },
                onIrAUtp = { navController.navigate(Screen.UtpHome.route) }
            )
        }

        // 3. Jefatura UTP
        composable(Screen.UtpHome.route) {
            UtpHomeScreen(
                onVolver = { navController.popBackStack() }
            )
        }

        // 4. Docente
        composable(Screen.DocenteHome.route) {
            DocenteHomeScreen(
                onVolver = { navController.popBackStack() },
                onVerOa = { navController.navigate(Screen.OaLista.route) }
            )
        }

        // 5. Estudiante
        composable(Screen.EstudianteHome.route) {
            EstudianteHomeScreen(
                onVolver = { navController.popBackStack() },
                onVerOa = { navController.navigate(Screen.OaLista.route) }
            )
        }

        // 6. Lista de OAs
        composable(Screen.OaLista.route) {
            OaListaScreen(
                onVolver = { navController.popBackStack() },
                onRealizarPrueba = { navController.navigate(Screen.PruebaEstudiante.route) }
            )
        }

        // 7. Evaluación
        composable(Screen.PruebaEstudiante.route) {
            PruebaEstudianteScreen(
                onVolver = { navController.popBackStack() }
            )
        }
    }
}