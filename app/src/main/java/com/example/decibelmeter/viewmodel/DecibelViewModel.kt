package com.example.decibelmeter.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.decibelmeter.audio.AudioRecorder
import com.example.decibelmeter.data.CalibrationDataStore
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DecibelViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val audioRecorder =
        AudioRecorder(application)

    private val calibrationDataStore =
        CalibrationDataStore(application)

    /*
     * Decibelios actuales
     */
    private val _decibel =
        MutableStateFlow(0f)

    val decibel: StateFlow<Float> =
        _decibel.asStateFlow()

    /*
     * Estado de medición
     */
    private val _isMeasuring =
        MutableStateFlow(false)

    val isMeasuring: StateFlow<Boolean> =
        _isMeasuring.asStateFlow()

    /*
     * Historial
     */
    private val _history =
        MutableStateFlow<List<Float>>(
            emptyList()
        )

    val history: StateFlow<List<Float>> =
        _history.asStateFlow()

    /*
     * Estadísticas
     */
    private val _minimum =
        MutableStateFlow(0f)

    val minimum: StateFlow<Float> =
        _minimum.asStateFlow()

    private val _average =
        MutableStateFlow(0f)

    val average: StateFlow<Float> =
        _average.asStateFlow()

    private val _maximum =
        MutableStateFlow(0f)

    val maximum: StateFlow<Float> =
        _maximum.asStateFlow()

    /*
     * Calibración
     */
    private val _calibrationOffset =
        MutableStateFlow(0f)

    val calibrationOffset:
            StateFlow<Float> =
        _calibrationOffset.asStateFlow()

    /*
     * Trabajo de medición
     */
    private var measurementJob: Job? = null

    /*
     * Valor suavizado
     */
    private var smoothedValue = 0f

    init {

        loadCalibration()
    }

    /*
     * Cargar calibración guardada
     */
    private fun loadCalibration() {

        viewModelScope.launch {

            calibrationDataStore
                .calibrationOffset
                .collect { savedOffset ->

                    _calibrationOffset.value =
                        savedOffset
                }
        }
    }

    /*
     * Iniciar medición
     */
    fun startMeasuring() {

        if (_isMeasuring.value) {
            return
        }

        audioRecorder.start()

        _isMeasuring.value = true

        /*
         * Reiniciar valores
         * de esta medición
         */
        smoothedValue = 0f

        _minimum.value = 0f
        _average.value = 0f
        _maximum.value = 0f

        _history.value =
            emptyList()

        measurementJob =
            viewModelScope.launch {

                while (_isMeasuring.value) {

                    /*
                     * Lectura original
                     * del micrófono
                     */
                    val rawValue =
                        audioRecorder
                            .readDecibels()

                    /*
                     * Aplicar calibración
                     */
                    val calibratedValue =
                        (
                                rawValue +
                                        _calibrationOffset.value
                                )
                            .coerceIn(
                                0f,
                                120f
                            )

                    /*
                     * Suavizado
                     */
                    smoothedValue =
                        smoothedValue * 0.80f +
                                calibratedValue * 0.20f

                    /*
                     * Valor actual
                     */
                    _decibel.value =
                        smoothedValue

                    /*
                     * Actualizar historial
                     */
                    val updatedHistory =
                        (
                                _history.value +
                                        smoothedValue
                                ).takeLast(60)

                    _history.value =
                        updatedHistory

                    /*
                     * Actualizar estadísticas
                     */
                    if (
                        updatedHistory
                            .isNotEmpty()
                    ) {

                        _minimum.value =
                            updatedHistory
                                .minOrNull()
                                ?: 0f

                        _maximum.value =
                            updatedHistory
                                .maxOrNull()
                                ?: 0f

                        _average.value =
                            updatedHistory
                                .average()
                                .toFloat()
                    }

                    delay(100)
                }
            }
    }

    /*
     * Detener medición
     */
    fun stopMeasuring() {

        _isMeasuring.value =
            false

        measurementJob?.cancel()

        measurementJob = null

        audioRecorder.stop()
    }

    /*
     * Cambiar calibración
     */
    fun setCalibrationOffset(
        offset: Float
    ) {

        val newOffset =
            offset.coerceIn(
                -30f,
                30f
            )

        _calibrationOffset.value =
            newOffset

        /*
         * Guardar inmediatamente
         * en DataStore
         */
        viewModelScope.launch {

            calibrationDataStore
                .saveCalibrationOffset(
                    newOffset
                )
        }
    }

    override fun onCleared() {

        audioRecorder.stop()

        super.onCleared()
    }
}