package com.example.decibelmeter

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
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

    private val _decibel =
        MutableStateFlow(0f)

    val decibel: StateFlow<Float> =
        _decibel.asStateFlow()

    private val _isMeasuring =
        MutableStateFlow(false)

    val isMeasuring: StateFlow<Boolean> =
        _isMeasuring.asStateFlow()

    private val _history =
        MutableStateFlow<List<Float>>(emptyList())

    val history: StateFlow<List<Float>> =
        _history.asStateFlow()

    private var measurementJob: Job? = null

    private var smoothedValue = 0f

    fun startMeasuring() {

        if (_isMeasuring.value) {
            return
        }

        audioRecorder.start()

        _isMeasuring.value = true

        measurementJob =
            viewModelScope.launch {

                while (_isMeasuring.value) {

                    val rawValue =
                        audioRecorder.readDecibels()

                    smoothedValue =
                        smoothedValue * 0.80f +
                                rawValue * 0.20f

                    _decibel.value =
                        smoothedValue

                    _history.value =
                        (
                                _history.value +
                                        smoothedValue
                                ).takeLast(60)

                    delay(100)
                }
            }
    }

    fun stopMeasuring() {

        _isMeasuring.value = false

        measurementJob?.cancel()

        measurementJob = null

        audioRecorder.stop()
    }

    override fun onCleared() {

        audioRecorder.stop()

        super.onCleared()
    }
}