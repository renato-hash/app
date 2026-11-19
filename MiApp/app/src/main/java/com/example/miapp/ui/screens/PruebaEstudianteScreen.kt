package com.example.miapp.ui.screens

import android.speech.tts.TextToSpeech
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.miapp.data.OaEntity
import com.example.miapp.ui.components.AudioDidacticoButton
import java.util.Locale

private data class PreguntaDidactica(
    val enunciado: String,
    val opciones: List<String>,
    val indiceCorrecto: Int
)

private data class DesafioCalentamiento(
    val consigna: String,
    val opciones: List<String>,
    val indiceCorrecto: Int,
    val pista: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PruebaEstudianteScreen(
    oaActual: OaEntity?,
    modoLecturaFacil: Boolean = false,
    onGuardarResultado: (
        oa: OaEntity,
        puntajeObtenido: Int,
        puntajeTotal: Int,
        detalleRespuestas: String,
        onCallback: (Boolean, String) -> Unit
    ) -> Unit,
    onVolver: () -> Unit
) {
    val context = LocalContext.current

    val oaActivo = oaActual ?: OaEntity(
        id = 0,
        codigo = "OA-01",
        titulo = "Comprensión y Resolución Básica",
        asignatura = "Lenguaje y Comunicación",
        descripcion = "Lee con atención cada pregunta y marca la alternativa correcta.",
        fechaModificacion = "Hoy",
        intentosMaximos = 3
    )

    val fondoTarjeta = if (modoLecturaFacil) Color(0xFFFFF8E1) else MaterialTheme.colorScheme.surfaceVariant
    val bordeTarjeta = if (modoLecturaFacil) BorderStroke(2.dp, Color(0xFF1A237E)) else null
    val colorTexto = if (modoLecturaFacil) Color(0xFF0D1B2A) else MaterialTheme.colorScheme.onSurface
    val tamanoPregunta = if (modoLecturaFacil) 21.sp else 17.sp
    val tamanoOpcion = if (modoLecturaFacil) 19.sp else 16.sp

    // Carga asíncrona del motor de voz para no congelar la pantalla
    var ttsCelebracion by remember { mutableStateOf<TextToSpeech?>(null) }
    var vozLista by remember { mutableStateOf(false) }

    LaunchedEffect(context) {
        var engine: TextToSpeech? = null
        engine = TextToSpeech(context.applicationContext) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val res = engine?.setLanguage(Locale("es", "CL"))
                if (res == TextToSpeech.LANG_MISSING_DATA || res == TextToSpeech.LANG_NOT_SUPPORTED) {
                    engine?.setLanguage(Locale("es", "ES"))
                }
                engine?.setSpeechRate(0.92f)
                vozLista = true
            }
        }
        ttsCelebracion = engine
    }

    DisposableEffect(Unit) {
        onDispose {
            ttsCelebracion?.stop()
            ttsCelebracion?.shutdown()
        }
    }

    val desafio = remember(oaActivo.asignatura) {
        when {
            oaActivo.asignatura.contains("Matem", ignoreCase = true) -> DesafioCalentamiento(
                consigna = "Completa la secuencia antes de empezar: 10 - 20 - ___ - 40",
                opciones = listOf("25", "30", "35"),
                indiceCorrecto = 1,
                pista = "¡Muy bien! Vamos contando de 10 en 10."
            )
            oaActivo.asignatura.contains("Ciencia", ignoreCase = true) -> DesafioCalentamiento(
                consigna = "Une el elemento con su función: El sol entrega a las plantas...",
                opciones = listOf("Luz y calor", "Nieve", "Oscuridad"),
                indiceCorrecto = 0,
                pista = "¡Exacto! La luz del sol ayuda a crecer a las plantas."
            )
            oaActivo.asignatura.contains("Historia", ignoreCase = true) -> DesafioCalentamiento(
                consigna = "Ordena nuestra comunidad de menor a mayor: Sala -> Escuela -> ___",
                opciones = listOf("Comuna de San Bernardo", "Mochila", "Cuaderno"),
                indiceCorrecto = 0,
                pista = "¡Genial! Nuestra escuela está en la comuna de San Bernardo."
            )
            else -> DesafioCalentamiento(
                consigna = "Encuentra la palabra que rima con 'Escuela':",
                opciones = listOf("Acuarela", "Lápiz", "Libro"),
                indiceCorrecto = 0,
                pista = "¡Excelente! Escuela y acuarela terminan igual."
            )
        }
    }

    val preguntas = remember(oaActivo.asignatura) {
        when {
            oaActivo.asignatura.contains("Matem", ignoreCase = true) -> listOf(
                PreguntaDidactica("1. Si tienes 14 lápices y te regalan 5 más, ¿cuántos tienes?", listOf("18 lápices", "19 lápices", "20 lápices"), 1),
                PreguntaDidactica("2. ¿Qué número viene justo antes del 50?", listOf("49", "51", "40"), 0),
                PreguntaDidactica("3. ¿Cuántas decenas hay en el número 30?", listOf("30 decenas", "3 decenas", "10 decenas"), 1)
            )
            oaActivo.asignatura.contains("Ciencia", ignoreCase = true) -> listOf(
                PreguntaDidactica("1. ¿Qué necesitan las plantas para fabricar su alimento?", listOf("Luz del sol, agua y aire", "Solo piedras", "Oscuridad total"), 0),
                PreguntaDidactica("2. ¿Cuál de estos animales es un vertebrado?", listOf("Mariposa", "Perro", "Caracol"), 1),
                PreguntaDidactica("3. ¿En qué estado está el agua de un río?", listOf("Sólido", "Gaseoso", "Líquido"), 2)
            )
            oaActivo.asignatura.contains("Historia", ignoreCase = true) -> listOf(
                PreguntaDidactica("1. ¿En qué país se encuentra nuestra escuela de San Bernardo?", listOf("Chile", "Perú", "Argentina"), 0),
                PreguntaDidactica("2. ¿Qué institución ayuda a cuidar la salud de las personas?", listOf("El correo", "El hospital o CESFAM", "La biblioteca"), 1),
                PreguntaDidactica("3. ¿Cuál es un deber importante en la sala de clases?", listOf("Respetar a compañeros y docentes", "Interrumpir siempre", "No compartir"), 0)
            )
            else -> listOf(
                PreguntaDidactica("1. En el cuento 'Papelucho', ¿quién escribe su diario de vida?", listOf("Marcela", "Papelucho", "El profesor"), 1),
                PreguntaDidactica("2. ¿Cuál palabra es un sustantivo propio?", listOf("escuela", "cuaderno", "San Bernardo"), 2),
                PreguntaDidactica("3. ¿Con qué signo termina una pregunta?", listOf("Signo de interrogación (?)", "Punto final (.)", "Coma (,)"), 0)
            )
        }
    }

    var opcionDesafioSeleccionada by remember { mutableStateOf<Int?>(null) }
    val respuestasSeleccionadas = remember { mutableStateMapOf<Int, Int>() }
    var mensajeResultado by remember { mutableStateOf<String?>(null) }
    var evaluacionEnviada by remember { mutableStateOf(false) }
    var mostrarCelebracion by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Prueba Final • ${oaActivo.codigo}") })
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = oaActivo.titulo,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Tienes hasta ${oaActivo.intentosMaximos} intentos. Primero practica con el desafío rápido y luego responde las 3 preguntas.",
                            fontSize = tamanoOpcion
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        AudioDidacticoButton(
                            textoALeer = "Prueba final de ${oaActivo.asignatura}. ${oaActivo.titulo}. Primero resuelve el desafío de calentamiento y luego responde las tres preguntas.",
                            etiquetaBoton = "Escuchar guía de la prueba"
                        )
                    }
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
                    border = BorderStroke(2.dp, Color(0xFF2E7D32)),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "🎯 Desafío de Calentamiento (Práctica)",
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1B5E20),
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = desafio.consigna,
                            fontSize = tamanoPregunta,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            desafio.opciones.forEachIndexed { idx, texto ->
                                FilterChip(
                                    selected = opcionDesafioSeleccionada == idx,
                                    onClick = { opcionDesafioSeleccionada = idx },
                                    label = { Text(texto, fontSize = 15.sp) },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                        opcionDesafioSeleccionada?.let { elegida ->
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = if (elegida == desafio.indiceCorrecto) desafio.pista
                                else "💡 Pista: Vuelve a intentarlo, ¡tú puedes!",
                                color = if (elegida == desafio.indiceCorrecto) Color(0xFF1B5E20) else Color(0xFFE65100),
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                        }
                    }
                }

                preguntas.forEachIndexed { indexPregunta, pregunta ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = fondoTarjeta),
                        border = bordeTarjeta
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = pregunta.enunciado,
                                fontSize = tamanoPregunta,
                                lineHeight = (tamanoPregunta.value + 6).sp,
                                fontWeight = FontWeight.SemiBold,
                                color = colorTexto
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            pregunta.opciones.forEachIndexed { indexOpcion, textoOpcion ->
                                val seleccionada = respuestasSeleccionadas[indexPregunta] == indexOpcion
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .selectable(
                                            selected = seleccionada,
                                            enabled = !evaluacionEnviada,
                                            onClick = { respuestasSeleccionadas[indexPregunta] = indexOpcion }
                                        )
                                        .padding(vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(
                                        selected = seleccionada,
                                        onClick = { if (!evaluacionEnviada) respuestasSeleccionadas[indexPregunta] = indexOpcion }
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = textoOpcion,
                                        fontSize = tamanoOpcion,
                                        color = colorTexto
                                    )
                                }
                            }

                            if (evaluacionEnviada) {
                                val esCorrecta = respuestasSeleccionadas[indexPregunta] == pregunta.indiceCorrecto
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = if (esCorrecta) "✓ ¡Respuesta correcta!"
                                    else "✗ Incorrecta. Respuesta correcta: ${pregunta.opciones[pregunta.indiceCorrecto]}",
                                    color = if (esCorrecta) Color(0xFF1B5E20) else MaterialTheme.colorScheme.error,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = tamanoOpcion
                                )
                            }
                        }
                    }
                }

                mensajeResultado?.let { msg ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = if (mostrarCelebracion) Color(0xFFFFF59D) else MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            if (mostrarCelebracion) {
                                Text(
                                    text = "🎉 ¡FELICITACIONES, LO LOGRASTE! ⭐",
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 18.sp,
                                    color = Color(0xFFE65100)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                            }
                            Text(
                                text = msg,
                                fontSize = tamanoOpcion,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        }
                    }
                }

                Button(
                    onClick = {
                        if (respuestasSeleccionadas.size < preguntas.size) {
                            mensajeResultado = "Por favor responde las ${preguntas.size} preguntas antes de enviar."
                            return@Button
                        }

                        var correctas = 0
                        val detalles = mutableListOf<String>()

                        preguntas.forEachIndexed { idx, preg ->
                            val ok = respuestasSeleccionadas[idx] == preg.indiceCorrecto
                            if (ok) correctas++
                            detalles.add("P${idx + 1}: ${if (ok) "Correcta" else "Incorrecta"}")
                        }

                        onGuardarResultado(
                            oaActivo,
                            correctas,
                            preguntas.size,
                            detalles.joinToString(" | ")
                        ) { guardoOk, mensaje ->
                            evaluacionEnviada = true
                            mensajeResultado = mensaje
                            if (guardoOk && correctas >= 2) {
                                mostrarCelebracion = true
                                if (vozLista) {
                                    ttsCelebracion?.speak(
                                        "¡Felicitaciones! Ganaste $correctas estrellas en ${oaActivo.asignatura}.",
                                        TextToSpeech.QUEUE_FLUSH,
                                        null,
                                        "celebracion_id"
                                    )
                                }
                            }
                        }
                    },
                    enabled = !evaluacionEnviada,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                ) {
                    Text("Enviar Respuestas y Ganar Estrellas", fontSize = tamanoOpcion, fontWeight = FontWeight.Bold)
                }

                OutlinedButton(
                    onClick = onVolver,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Volver")
                }
            }

            // La animación solo se ejecuta cuando el alumno aprueba
            if (mostrarCelebracion) {
                LluviaConfeti()
            }
        }
    }
}

@Composable
private fun LluviaConfeti() {
    val transicionInfinita = rememberInfiniteTransition(label = "confeti")
    val progresoCaida by transicionInfinita.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "caida"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val colores = listOf(Color(0xFFFFB300), Color(0xFF43A047), Color(0xFF1E88E5), Color(0xFFE53935), Color(0xFF8E24AA))
        for (i in 0 until 28) {
            val x = (size.width / 28f) * i + 12f
            val desplazamiento = ((progresoCaida + (i * 0.13f)) % 1f) * size.height
            drawCircle(
                color = colores[i % colores.size],
                radius = 10f,
                center = Offset(x, desplazamiento)
            )
        }
    }
}