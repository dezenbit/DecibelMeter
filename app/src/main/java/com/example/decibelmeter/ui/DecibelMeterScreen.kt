package com.example.decibelmeter.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.decibelmeter.ui.components.DecibelGauge
import com.example.decibelmeter.ui.components.DecibelGraph
import com.example.decibelmeter.ui.components.MeasurementButton
import com.example.decibelmeter.ui.components.MeasurementIndicator
import com.example.decibelmeter.ui.components.StatisticsCard
import com.example.decibelmeter.viewmodel.DecibelViewModel

@Composable
fun DecibelMeterScreen(
    viewModel: DecibelViewModel,
    onSettingsClick: () -> Unit,
    onStart: () -> Unit
) {

    val decibel by
    viewModel.decibel.collectAsState()

    val isMeasuring by
    viewModel.isMeasuring.collectAsState()

    val history by
    viewModel.history.collectAsState()

    val minimum by
    viewModel.minimum.collectAsState()

    val average by
    viewModel.average.collectAsState()

    val maximum by
    viewModel.maximum.collectAsState()

    val levelColor =
        getDecibelColor(decibel)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Color(0xFF0D1117)
            )
            .verticalScroll(
                rememberScrollState()
            )
            .padding(24.dp),

        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {

        /*
         * Barra superior
         */
        androidx.compose.foundation.layout.Row(
            modifier =
                Modifier.fillMaxWidth(),

            horizontalArrangement =
                Arrangement.End,

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            IconButton(
                onClick = onSettingsClick
            ) {

                Icon(
                    imageVector =
                        Icons.Default.Settings,

                    contentDescription =
                        "Ajustes",

                    tint = Color.White
                )
            }
        }

        Spacer(
            modifier =
                Modifier.height(5.dp)
        )

        /*
         * Título
         */
        Text(
            text = "DECIBEL METER",

            color = Color.White,

            fontSize = 16.sp,

            fontWeight =
                FontWeight.Bold,

            letterSpacing = 3.sp
        )

        Spacer(
            modifier =
                Modifier.height(12.dp)
        )

        /*
         * Descripción del sonido
         */
        Text(
            text =
                getSoundDescription(
                    decibel
                ),

            color =
                Color(0xFF9BA3AF),

            fontSize = 16.sp
        )

        Spacer(
            modifier =
                Modifier.height(8.dp)
        )

        /*
         * Indicador de medición
         */
        MeasurementIndicator(
            isMeasuring =
                isMeasuring
        )

        Spacer(
            modifier =
                Modifier.height(25.dp)
        )

        /*
         * Medidor principal
         */
        DecibelGauge(
            decibel = decibel,
            color = levelColor
        )

        Spacer(
            modifier =
                Modifier.height(30.dp)
        )

        /*
         * Estadísticas
         */
        StatisticsCard(
            minimum = minimum,
            average = average,
            maximum = maximum
        )

        Spacer(
            modifier =
                Modifier.height(20.dp)
        )

        /*
         * Gráfica
         */
        DecibelGraph(
            values = history,
            color = levelColor
        )

        Spacer(
            modifier =
                Modifier.height(25.dp)
        )

        /*
         * Botón de medición
         */
        MeasurementButton(
            isMeasuring =
                isMeasuring,

            onClick = {

                if (isMeasuring) {

                    viewModel.stopMeasuring()

                } else {

                    onStart()
                }
            }
        )

        Spacer(
            modifier =
                Modifier.height(20.dp)
        )
    }
}


/*
 * Color según el nivel de sonido
 */
fun getDecibelColor(
    decibel: Float
): Color {

    return when {

        decibel < 40 ->
            Color(0xFF22C55E)

        decibel < 70 ->
            Color(0xFFEAB308)

        decibel < 90 ->
            Color(0xFFF97316)

        else ->
            Color(0xFFEF4444)
    }
}


/*
 * Descripción del nivel de sonido
 */
fun getSoundDescription(
    decibel: Float
): String {

    return when {

        decibel < 20 ->
            "Muy silencioso"

        decibel < 40 ->
            "Silencioso"

        decibel < 60 ->
            "Normal"

        decibel < 80 ->
            "Ruidoso"

        decibel < 100 ->
            "Muy ruidoso"

        else ->
            "Extremadamente ruidoso"
    }
}