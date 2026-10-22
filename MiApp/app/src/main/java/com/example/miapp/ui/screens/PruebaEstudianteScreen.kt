package com.example.miapp.ui.screens

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.speech.tts.TextToSpeech
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.miapp.model.Pregunta
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PruebaEstudianteScreen(onVolver: () -> Unit) {
    val context = LocalContext.current
    var tts by remember { mutableStateOf<TextToSpeech?>(null) }

    DisposableEffect(Unit) {
        val textToSpeech = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.language = Locale("es", "ES")
            }
        }
        tts = textToSpeech
        onDispose {
            textToSpeech.stop()
            textToSpeech.shutdown()
        }
    }

    fun vibrar() {
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(100)
        }
    }

    // Lista de preguntas de la evaluación
    val listaPreguntas = remember {
        listOf(
            Pregunta(
                id = 1,
                enunciado = "¿Cuál es el objetivo principal de la lectura comprensiva?",
                opciones = listOf(
                    "A) Comprender la idea principal del texto",
                    "B) Identificar personajes secundarios únicamente",
                    "C) Memorizar todas las palabras sin entenderlas"
                ),
                respuestaCorrecta = 0
            ),
            Pregunta(
                id = 2,
                enunciado = "¿Qué elemento nos ayuda a saber de qué trata un cuento antes de leerlo?",
                opciones = listOf(
                    "A) El número de páginas",
                    "B) El título y las ilustraciones",
                    "C) El tipo de letra utilizada"
                ),
                respuestaCorrecta = 1
            ),
            Pregunta(
                id = 3,
                enunciado = "¿Qué debemos hacer al encontrar una palabra desconocida en la lectura?",
                opciones = listOf(
                    "A) Saltarla y no volver a leer",
                    "B) Cerrar el libro de inmediato",
                    "C) Deduciendo su significado por el contexto o consultar un diccionario"
                ),
                respuestaCorrecta = 2
            )
        )
    }

    // Estados del flujo de la prueba
    var indiceActual by remember { mutableIntStateOf(0) }
    var opcionSeleccionada by remember { mutableIntStateOf(-1) }
    var respuestaEnviada by remember { mutableStateOf(false) }
    var puntaje by remember { mutableIntStateOf(0) }
    var pruebaFinalizada by remember { mutableStateOf(false) }
    var mensajeResultado by remember { mutableStateOf("") }

    val preguntaActual = listaPreguntas[indiceActual]
    val progreso = (indiceActual + 1).toFloat() / listaPreguntas.size.toFloat()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Evaluación OA 01 - 2.° Básico") }) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            if (!pruebaFinalizada) {
                // Barra de Progreso Superior
                Text(
                    text = "Pregunta ${indiceActual + 1} de ${listaPreguntas.size}",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(6.dp))
                LinearProgressIndicator(
                    progress = { progreso },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Encabezado con Botón de Audio
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Pregunta #${preguntaActual.id}",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Button(onClick = {
                        vibrar()
                        tts?.speak(preguntaActual.enunciado, TextToSpeech.QUEUE_FLUSH, null, null)
                    }) {
                        Text("🔊 Escuchar")
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                Text(preguntaActual.enunciado, style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(16.dp))

                // Alternativas
                preguntaActual.opciones.forEachIndexed { index, opcion ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        RadioButton(
                            selected = (opcionSeleccionada == index),
                            onClick = {
                                if (!respuestaEnviada) {
                                    vibrar()
                                    opcionSeleccionada = index
                                }
                            }
                        )
                        Text(opcion, modifier = Modifier.padding(start = 8.dp))
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Botón Enviar Respuesta
                if (!respuestaEnviada) {
                    Button(
                        onClick = {
                            if (opcionSeleccionada != -1) {
                                vibrar()
                                respuestaEnviada = true
                                if (opcionSeleccionada == preguntaActual.respuestaCorrecta) {
                                    puntaje++
                                    mensajeResultado = "¡Respuesta Correcta! Excelente trabajo."
                                } else {
                                    mensajeResultado = "Respuesta Incorrecta."
                                }
                                tts?.speak(mensajeResultado, TextToSpeech.QUEUE_FLUSH, null, null)
                            }
                        },
                        enabled = opcionSeleccionada != -1,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Confirmar Respuesta")
                    }
                } else {
                    // Animación de Retroalimentación y Botón Siguiente / Finalizar
                    AnimatedVisibility(
                        visible = respuestaEnviada,
                        enter = fadeIn() + slideInVertically()
                    ) {
                        Column {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (opcionSeleccionada == preguntaActual.respuestaCorrecta)
                                        MaterialTheme.colorScheme.primaryContainer
                                    else
                                        MaterialTheme.colorScheme.errorContainer
                                )
                            ) {
                                Text(
                                    text = mensajeResultado,
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier.padding(12.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Button(
                                onClick = {
                                    vibrar()
                                    if (indiceActual < listaPreguntas.size - 1) {
                                        indiceActual++
                                        opcionSeleccionada = -1
                                        respuestaEnviada = false
                                        mensajeResultado = ""
                                    } else {
                                        pruebaFinalizada = true
                                        val resumen = "Evaluación finalizada. Obtuviste $puntaje de ${listaPreguntas.size} puntos."
                                        tts?.speak(resumen, TextToSpeech.QUEUE_FLUSH, null, null)
                                    }
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    if (indiceActual < listaPreguntas.size - 1) "Siguiente Pregunta ➔"
                                    else "Ver Resultado Final 🏆"
                                )
                            }
                        }
                    }
                }
            } else {
                // Tarjeta de Resultado Final
                Card(
                    modifier = Modifier.fillMaxWidth().padding(top = 24.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("🎉 ¡Evaluación Completada!", style = MaterialTheme.typography.headlineMedium)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Puntaje obtenido: $puntaje / ${listaPreguntas.size}",
                            style = MaterialTheme.typography.titleLarge
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = if (puntaje == listaPreguntas.size) "¡Excelente! Logro 100% alcanzado." else "¡Buen intento! Puedes volver a practicar.",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        vibrar()
                        indiceActual = 0
                        opcionSeleccionada = -1
                        respuestaEnviada = false
                        puntaje = 0
                        pruebaFinalizada = false
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Reintentar Evaluación")
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            OutlinedButton(onClick = onVolver, modifier = Modifier.fillMaxWidth()) {
                Text("Volver a Lista de OAs")
            }
        }
    }
}