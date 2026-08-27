package com.example.decibelmeter.ui.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MeasurementIndicator(
    isMeasuring: Boolean
) {

    if (!isMeasuring) {
        return
    }

    val infiniteTransition =
        rememberInfiniteTransition(
            label = "measurementPulse"
        )

    val alpha by
    infiniteTransition.animateFloat(
        initialValue = 0.35f,
        targetValue = 1f,

        animationSpec =
            infiniteRepeatable(
                animation =
                    tween(700),
                repeatMode =
                    RepeatMode.Reverse
            ),

        label = "indicatorAlpha"
    )

    Row(
        verticalAlignment =
            Alignment.CenterVertically,

        horizontalArrangement =
            Arrangement.spacedBy(8.dp)
    ) {

        Canvas(
            modifier =
                Modifier.size(10.dp)
        ) {

            drawCircle(
                color =
                    Color(0xFFEF4444)
                        .copy(alpha = alpha)
            )
        }

        Text(
            text = "MIDIENDO",

            color =
                Color(0xFFEF4444)
                    .copy(alpha = alpha),

            fontSize = 13.sp
        )
    }
}