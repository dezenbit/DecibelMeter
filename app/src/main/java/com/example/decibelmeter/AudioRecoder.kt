package com.example.decibelmeter

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import androidx.core.content.ContextCompat
import kotlin.math.log10
import kotlin.math.sqrt

class AudioRecorder(
    private val context: Context
) {

    private var audioRecord: AudioRecord? = null
    private var bufferSize = 0

    fun start() {

        // Comprobar permiso antes de acceder al micrófono
        val permissionGranted =
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED

        if (!permissionGranted) {
            throw SecurityException(
                "El permiso RECORD_AUDIO no ha sido concedido."
            )
        }

        val sampleRate = 44100

        bufferSize = AudioRecord.getMinBufferSize(
            sampleRate,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT
        )

        if (bufferSize <= 0) {
            throw IllegalStateException(
                "No se pudo obtener un buffer válido para el micrófono."
            )
        }

        audioRecord = AudioRecord(
            MediaRecorder.AudioSource.DEFAULT,
            sampleRate,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT,
            bufferSize * 2
        )

        audioRecord?.startRecording()
    }

    fun readDecibels(): Float {

        val recorder = audioRecord ?: return 0f

        val buffer = ShortArray(bufferSize)

        val read = recorder.read(
            buffer,
            0,
            buffer.size
        )

        if (read <= 0) {
            return 0f
        }

        var sum = 0.0

        for (i in 0 until read) {

            val sample =
                buffer[i].toDouble()

            sum += sample * sample
        }

        val rms =
            sqrt(sum / read)

        if (rms <= 0) {
            return 0f
        }

        val reference = 32767.0

        val db =
            20 * log10(
                rms / reference
            )

        /*
         * Esto es una aproximación visual.
         *
         * NO representa dB SPL calibrados.
         */
        val approximateDb =
            db + 100

        return approximateDb
            .toFloat()
            .coerceIn(0f, 120f)
    }

    fun stop() {

        audioRecord?.apply {

            if (
                recordingState ==
                AudioRecord.RECORDSTATE_RECORDING
            ) {
                stop()
            }

            release()
        }

        audioRecord = null
    }
}