package com.example.miapp.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.miapp.data.OaEntity
import com.example.miapp.data.ProgresoEntity
import com.example.miapp.ui.components.AudioDidacticoButton
import com.example.miapp.ui.components.FeriadosCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EstudianteHomeScreen(
    oasPublicados: List<OaEntity>,
    listaProgreso: List<ProgresoEntity>,
    modoLecturaFacil: Boolean,
    onToggleLecturaFacil: (Boolean) -> Unit,
    onSeleccionarOaParaPrueba: (OaEntity) -> Unit,
    onVolver: () -> Unit,
    onVerOa: () -> Unit
) {
    val fondoTarjeta = if (modoLecturaFacil) Color(0xFFFFF8E1) else MaterialTheme.colorScheme.surfaceVariant
    val bordeTarjeta = if (modoLecturaFacil) BorderStroke(2.dp, Color(0xFF1A237E)) else null
    val colorTextoPrincipal = if (modoLecturaFacil) Color(0xFF0D1B2A) else MaterialTheme.colorScheme.onSurface
    val tamanoTitulo = if (modoLecturaFacil) 24.sp else 20.sp
    val tamanoCuerpo = if (modoLecturaFacil) 19.sp else 15.sp

    val totalEstrellas = listaProgreso.sumOf { it.puntajeObtenido }
    val logroLenguaje = listaProgreso.any { it.asignatura.contains("Lenguaje", true) && it.porcentajeAvance >= 60 }
    val logroMatematica = listaProgreso.any { it.asignatura.contains("Matem", true) && it.porcentajeAvance >= 60 }
    val logroCiencias = listaProgreso.any { it.asignatura.contains("Ciencia", true) && it.porcentajeAvance >= 60 }
    val logroHistoria = listaProgreso.any { it.asignatura.contains("Historia", true) && it.porcentajeAvance >= 60 }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("MPAZ PRO • 2.° Básico") })
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = if (modoLecturaFacil) Color(0xFF1A237E) else MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Modo Lectura Fácil",
                                fontWeight = FontWeight.Bold,
                                fontSize = 17.sp,
                                color = if (modoLecturaFacil) Color.White else MaterialTheme.colorScheme.onSecondaryContainer
                            )
                            Text(
                                text = "Letra grande y alto contraste",
                                fontSize = 13.sp,
                                color = if (modoLecturaFacil) Color(0xFFFFE082) else MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                        Switch(
                            checked = modoLecturaFacil,
                            onCheckedChange = onToggleLecturaFacil
                        )
                    }
                }
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Mi Pasaporte de Logros",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Surface(
                                color = Color(0xFFFFB300),
                                shape = RoundedCornerShape(20.dp)
                            ) {
                                Text(
                                    text = "⭐ $totalEstrellas Estrellas",
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            MedallaItem(emoji = "📚", nombre = "Lector", desbloqueada = logroLenguaje)
                            MedallaItem(emoji = "🔢", nombre = "Números", desbloqueada = logroMatematica)
                            MedallaItem(emoji = "🌱", nombre = "Ciencia", desbloqueada = logroCiencias)
                            MedallaItem(emoji = "🏛️", nombre = "Historia", desbloqueada = logroHistoria)
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        AudioDidacticoButton(
                            textoALeer = "Hola estudiante de segundo básico. Llevas $totalEstrellas estrellas ganadas. Elige una actividad para escuchar su explicación y jugar.",
                            etiquetaBoton = "Escuchar mi progreso"
                        )
                    }
                }
            }

            item {
                FeriadosCard()
            }

            item {
                Text(
                    text = "Mis Asignaturas y Actividades",
                    fontSize = tamanoTitulo,
                    fontWeight = FontWeight.Bold
                )
            }

            if (oasPublicados.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = fondoTarjeta),
                        border = bordeTarjeta
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Aún no tienes actividades publicadas por UTP. Puedes entrar a explorar:",
                                fontSize = tamanoCuerpo,
                                color = colorTextoPrincipal
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Button(
                                onClick = onVerOa,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Explorar Actividades", fontSize = tamanoCuerpo)
                            }
                        }
                    }
                }
            } else {
                items(oasPublicados) { oa ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = fondoTarjeta),
                        border = bordeTarjeta
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "${oa.asignatura.uppercase()} • ${oa.codigo}",
                                style = MaterialTheme.typography.labelLarge,
                                color = if (modoLecturaFacil) Color(0xFF1A237E) else MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = oa.titulo,
                                fontSize = tamanoTitulo,
                                lineHeight = (tamanoTitulo.value + 6).sp,
                                fontWeight = FontWeight.Bold,
                                color = colorTextoPrincipal
                            )
                            if (oa.descripcion.isNotBlank()) {
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = oa.descripcion,
                                    fontSize = tamanoCuerpo,
                                    lineHeight = (tamanoCuerpo.value + 6).sp,
                                    color = colorTextoPrincipal
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Intentos permitidos: ${oa.intentosMaximos}",
                                fontSize = 14.sp,
                                color = colorTextoPrincipal
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            AudioDidacticoButton(
                                textoALeer = "${oa.asignatura}. ${oa.titulo}. ${oa.descripcion}",
                                etiquetaBoton = "Escuchar explicación"
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Button(
                                onClick = { onSeleccionarOaParaPrueba(oa) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(54.dp)
                            ) {
                                Icon(Icons.Default.Star, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Jugar y Rendir Prueba", fontSize = tamanoCuerpo, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            item {
                OutlinedButton(
                    onClick = onVolver,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Volver a Selección de Perfil")
                }
            }
        }
    }
}

@Composable
private fun MedallaItem(
    emoji: String,
    nombre: String,
    desbloqueada: Boolean
) {
    Surface(
        color = if (desbloqueada) Color(0xFFFFF59D) else Color.White.copy(alpha = 0.55f),
        border = BorderStroke(
            width = if (desbloqueada) 2.dp else 1.dp,
            color = if (desbloqueada) Color(0xFFF57F17) else Color.Gray.copy(alpha = 0.4f)
        ),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.width(74.dp)
    ) {
        Column(
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = if (desbloqueada) emoji else "🔒", fontSize = 22.sp)
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = nombre,
                fontSize = 12.sp,
                fontWeight = if (desbloqueada) FontWeight.Bold else FontWeight.Normal,
                color = Color.Black
            )
        }
    }
}

