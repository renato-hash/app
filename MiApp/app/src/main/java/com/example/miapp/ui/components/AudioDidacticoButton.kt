package com.example.miapp.ui.components

import android.content.Context
import android.speech.tts.TextToSpeech
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.util.Locale

object TtsManager {
    private var tts: TextToSpeech? = null
    private var listo = false
    private var inicializando = false
    private var textoPendiente: String? = null

    fun hablar(context: Context, texto: String) {
        if (listo && tts != null) {
            tts?.stop()
            tts?.speak(texto, TextToSpeech.QUEUE_FLUSH, null, "mpaz_audio")
            return
        }

        textoPendiente = texto
        if (!inicializando) {
            inicializando = true
            tts = TextToSpeech(context.applicationContext) { status ->
                inicializando = false
                if (status == TextToSpeech.SUCCESS) {
                    val res = tts?.setLanguage(Locale("es", "CL"))
                    if (res == TextToSpeech.LANG_MISSING_DATA || res == TextToSpeech.LANG_NOT_SUPPORTED) {
                        tts?.setLanguage(Locale("es", "ES"))
                    }
                    tts?.setSpeechRate(0.90f)
                    listo = true
                    textoPendiente?.let { pendiente ->
                        tts?.speak(pendiente, TextToSpeech.QUEUE_FLUSH, null, "mpaz_audio")
                        textoPendiente = null
                    }
                }
            }
        }
    }

    fun detener() {
        textoPendiente = null
        if (listo) {
            tts?.stop()
        }
    }
}

@Composable
fun AudioDidacticoButton(
    textoALeer: String,
    etiquetaBoton: String = "Escuchar explicación en voz alta",
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    DisposableEffect(Unit) {
        onDispose {
            TtsManager.detener()
        }
    }

    Button(
        onClick = { TtsManager.hablar(context, textoALeer) },
        modifier = modifier
            .fillMaxWidth()
            .height(54.dp),
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.secondary
        )
    ) {
        Icon(
            imageVector = Icons.Default.PlayArrow,
            contentDescription = "Reproducir audio",
            modifier = Modifier.size(26.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = etiquetaBoton,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
    }
}