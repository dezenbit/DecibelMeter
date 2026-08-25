package com.example.decibelmeter

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.decibelmeter.ui.theme.DecibelMeterTheme

class MainActivity : ComponentActivity() {

    private val viewModel: DecibelViewModel by viewModels()

    private val microphonePermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { granted ->

            if (granted) {
                viewModel.startMeasuring()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            DecibelMeterTheme {

                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {

                    DecibelMeterApp(
                        viewModel = viewModel,
                        onStart = {
                            checkMicrophonePermission()
                        }
                    )
                }
            }
        }
    }

    private fun checkMicrophonePermission() {

        val permissionGranted =
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED

        if (permissionGranted) {

            viewModel.startMeasuring()

        } else {

            microphonePermissionLauncher.launch(
                Manifest.permission.RECORD_AUDIO
            )
        }
    }
}

@Composable
fun DecibelMeterApp(
    viewModel: DecibelViewModel,
    onStart: () -> Unit
) {

    val decibel by viewModel.decibel.collectAsState()

    val isMeasuring by
    viewModel.isMeasuring.collectAsState()

    val history by
    viewModel.history.collectAsState()

    val levelColor = getDecibelColor(decibel)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Color(0xFF0D1117)
            )
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(
            modifier = Modifier.height(30.dp)
        )

        Text(
            text = "DECIBEL METER",
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 3.sp
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        Text(
            text = getSoundDescription(decibel),
            color = Color(0xFF9BA3AF),
            fontSize = 16.sp
        )

        Spacer(
            modifier = Modifier.height(40.dp)
        )

        DecibelGauge(
            decibel = decibel,
            color = levelColor
        )

        Spacer(
            modifier = Modifier.height(45.dp)
        )

        SoundLevelCard(
            decibel = decibel,
            color = levelColor
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        HistoryGraph(
            values = history,
            color = levelColor
        )

        Spacer(
            modifier = Modifier.weight(1f)
        )

        Button(
            onClick = {

                if (isMeasuring) {
                    viewModel.stopMeasuring()
                } else {
                    onStart()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor =
                    if (isMeasuring)
                        Color(0xFFEF4444)
                    else
                        Color(0xFF22C55E)
            ),
            shape = RoundedCornerShape(20.dp)
        ) {

            Text(
                text =
                    if (isMeasuring)
                        "DETENER MEDICIÓN"
                    else
                        "INICIAR MEDICIÓN",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(
            modifier = Modifier.height(20.dp)
        )
    }
}

@Composable
fun DecibelGauge(
    decibel: Float,
    color: Color
) {

    val animatedValue by animateFloatAsState(
        targetValue = decibel,
        animationSpec = tween(
            durationMillis = 300
        ),
        label = "decibelAnimation"
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(270.dp)
    ) {

        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {

            val strokeWidth = 22.dp.toPx()

            val diameter =
                size.minDimension - strokeWidth

            val topLeft = Offset(
                (size.width - diameter) / 2,
                (size.height - diameter) / 2
            )

            drawArc(
                color = Color(0xFF252B36),
                startAngle = 135f,
                sweepAngle = 270f,
                useCenter = false,
                topLeft = topLeft,
                size = Size(
                    diameter,
                    diameter
                ),
                style = Stroke(
                    width = strokeWidth,
                    cap = StrokeCap.Round
                )
            )

            val progress =
                (animatedValue / 120f)
                    .coerceIn(0f, 1f)

            drawArc(
                color = color,
                startAngle = 135f,
                sweepAngle =
                    270f * progress,
                useCenter = false,
                topLeft = topLeft,
                size = Size(
                    diameter,
                    diameter
                ),
                style = Stroke(
                    width = strokeWidth,
                    cap = StrokeCap.Round
                )
            )
        }

        Column(
            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {

            Text(
                text =
                    String.format(
                        "%.1f",
                        animatedValue
                    ),
                color = Color.White,
                fontSize = 56.sp,
                fontWeight =
                    FontWeight.Bold
            )

            Text(
                text = "dB",
                color = Color(0xFF9BA3AF),
                fontSize = 20.sp
            )
        }
    }
}

@Composable
fun SoundLevelCard(
    decibel: Float,
    color: Color
) {

    Surface(
        modifier =
            Modifier.fillMaxWidth(),
        shape =
            RoundedCornerShape(22.dp),
        color =
            Color(0xFF161B22)
    ) {

        Column(
            modifier =
                Modifier.padding(20.dp),
            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {

            Text(
                text = "NIVEL DE SONIDO",
                color =
                    Color(0xFF9BA3AF),
                fontSize = 12.sp,
                letterSpacing = 2.sp
            )

            Spacer(
                modifier =
                    Modifier.height(8.dp)
            )

            Text(
                text =
                    getSoundDescription(
                        decibel
                    ),
                color = color,
                fontSize = 24.sp,
                fontWeight =
                    FontWeight.Bold
            )
        }
    }
}

@Composable
fun HistoryGraph(
    values: List<Float>,
    color: Color
) {

    Surface(
        modifier =
            Modifier
                .fillMaxWidth()
                .height(130.dp),
        shape =
            RoundedCornerShape(22.dp),
        color =
            Color(0xFF161B22)
    ) {

        Column(
            modifier =
                Modifier.padding(16.dp)
        ) {

            Text(
                text = "HISTORIAL",
                color =
                    Color(0xFF9BA3AF),
                fontSize = 12.sp,
                letterSpacing = 2.sp
            )

            Spacer(
                modifier =
                    Modifier.height(12.dp)
            )

            Canvas(
                modifier =
                    Modifier
                        .fillMaxSize()
            ) {

                if (
                    values.size < 2
                ) return@Canvas

                val maxValue = 120f

                val step =
                    size.width /
                            (values.size - 1)

                for (
                i in
                0 until
                        values.size - 1
                ) {

                    val x1 =
                        i * step

                    val x2 =
                        (i + 1) * step

                    val y1 =
                        size.height -
                                (
                                        values[i] /
                                                maxValue *
                                                size.height
                                        )

                    val y2 =
                        size.height -
                                (
                                        values[i + 1] /
                                                maxValue *
                                                size.height
                                        )

                    drawLine(
                        color = color,
                        start =
                            Offset(
                                x1,
                                y1
                            ),
                        end =
                            Offset(
                                x2,
                                y2
                            ),
                        strokeWidth =
                            5f,
                        cap =
                            StrokeCap.Round
                    )
                }
            }
        }
    }
}

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