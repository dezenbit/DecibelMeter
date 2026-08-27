package com.example.decibelmeter.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MeasurementButton(
    isMeasuring: Boolean,
    onClick: () -> Unit
) {

    val buttonColor by animateColorAsState(
        targetValue =
            if (isMeasuring) {
                Color(0xFFEF4444)
            } else {
                Color(0xFF22C55E)
            },
        animationSpec =
            tween(300),
        label = "buttonColor"
    )

    Button(
        onClick = onClick,

        modifier =
            Modifier
                .fillMaxWidth()
                .height(60.dp),

        colors =
            ButtonDefaults.buttonColors(
                containerColor =
                    buttonColor
            )
    ) {

        AnimatedContent(
            targetState = isMeasuring,
            transitionSpec = {
                fadeIn(
                    animationSpec =
                        tween(200)
                ) togetherWith
                        fadeOut(
                            animationSpec =
                                tween(200)
                        )
            },
            label = "buttonText"
        ) { measuring ->

            Text(
                text =
                    if (measuring) {
                        "DETENER MEDICIÓN"
                    } else {
                        "INICIAR MEDICIÓN"
                    },

                fontSize = 16.sp,

                fontWeight =
                    FontWeight.Bold
            )
        }
    }
}