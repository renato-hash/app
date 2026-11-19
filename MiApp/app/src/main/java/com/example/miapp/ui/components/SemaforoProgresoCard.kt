package com.example.miapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SemaforoProgresoCard(
    evaluacionesCompletadas: Int,
    puntajeObtenidoTotal: Int,
    puntajeMaximoTotal: Int,
    modifier: Modifier = Modifier
) {
    val porcentaje = if (puntajeMaximoTotal > 0) {
        (puntajeObtenidoTotal * 100) / puntajeMaximoTotal
    } else {
        0
    }

    val (estadoTexto, colorEstado, recomendacion) = when {
        evaluacionesCompletadas == 0 -> Triple(
            "Sin evaluaciones registradas",
            Color(0xFF546E7A),
            "Asigna un Objetivo de Aprendizaje para iniciar el seguimiento."
        )
        porcentaje >= 75 -> Triple(
            "Logrado ($porcentaje%)",
            Color(0xFF2E7D32),
            "Excelente dominio de los OA de 2.° Básico. Listo para avanzar."
        )
        porcentaje >= 50 -> Triple(
            "En Desarrollo ($porcentaje%)",
            Color(0xFFEF6C00),
            "Se sugiere repetir el Desafío de Calentamiento antes de la próxima prueba."
        )
        else -> Triple(
            "Requiere Refuerzo ($porcentaje%)",
            Color(0xFFC62828),
            "Activar Modo Lectura Fácil y apoyo con audio guiado en clase."
        )
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "📊 Indicador Pedagógico",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Surface(
                    color = colorEstado.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = estadoTexto,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        color = colorEstado,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 13.sp
                    )
                }
            }

            // Barra de progreso ligera
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .background(Color.LightGray.copy(alpha = 0.5f))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth((porcentaje / 100f).coerceIn(0f, 1f))
                        .fillMaxHeight()
                        .background(colorEstado)
                )
            }

            Text(
                text = "Evaluaciones rendidas: $evaluacionesCompletadas • Puntaje acumulado: $puntajeObtenidoTotal / $puntajeMaximoTotal",
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "Sugerencia didáctica: $recomendacion",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
