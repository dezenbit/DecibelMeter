package com.example.decibelmeter.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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

            val strokeWidth =
                22.dp.toPx()

            val diameter =
                size.minDimension -
                        strokeWidth

            val topLeft = Offset(
                (size.width - diameter) / 2,
                (size.height - diameter) / 2
            )

            /*
             * Arco de fondo.
             */
            drawArc(
                color =
                    Color(0xFF252B36),

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

            /*
             * Progreso actual.
             */
            val progress =
                (animatedValue / 120f)
                    .coerceIn(
                        0f,
                        1f
                    )

            /*
             * Gradiente del medidor.
             */
            val gradient =
                Brush.sweepGradient(
                    colors = listOf(
                        Color(0xFF22C55E),
                        Color(0xFFEAB308),
                        Color(0xFFF97316),
                        Color(0xFFEF4444)
                    ),

                    center = Offset(
                        size.width / 2f,
                        size.height / 2f
                    )
                )

            /*
             * Arco de progreso.
             */
            drawArc(
                brush = gradient,

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
                text = String.format(
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

                color =
                    Color(0xFF9BA3AF),

                fontSize = 20.sp
            )
        }
    }
}