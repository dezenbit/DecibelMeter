package com.example.decibelmeter.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import java.util.Locale

@Composable
fun CalibrationScreen(
    calibrationOffset: Float,
    onOffsetChange: (Float) -> Unit,
    onBack: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {

        TextButton(
            onClick = onBack
        ) {

            Text(
                text = "← Volver",
                color = Color.White
            )
        }

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Text(
            text = "CALIBRACIÓN",
            color = Color.White,
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        Text(
            text =
                "Ajusta la lectura para que coincida " +
                        "con una referencia conocida.",
            color = Color(0xFF9BA3AF),
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(
            modifier = Modifier.height(40.dp)
        )

        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color(0xFF161B22),
            shape = MaterialTheme.shapes.large
        ) {

            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment =
                    Alignment.CenterHorizontally
            ) {

                Text(
                    text = "CORRECCIÓN",
                    color = Color(0xFF9BA3AF),
                    style = MaterialTheme.typography.labelLarge
                )

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                Text(
                    text = String.format(
                        Locale.US,
                        "%+.1f dB",
                        calibrationOffset
                    ),
                    color = Color.White,
                    style = MaterialTheme.typography.displaySmall
                )

                Spacer(
                    modifier = Modifier.height(24.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement =
                        Arrangement.SpaceEvenly,
                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    Button(
                        onClick = {
                            onOffsetChange(
                                calibrationOffset - 0.5f
                            )
                        }
                    ) {
                        Text("−")
                    }

                    Button(
                        onClick = {
                            onOffsetChange(
                                calibrationOffset + 0.5f
                            )
                        }
                    ) {
                        Text("+")
                    }
                }
            }
        }

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        Text(
            text =
                "Ejemplo: si una referencia conocida " +
                        "es 60 dB y la aplicación muestra " +
                        "56 dB, utiliza una corrección de +4 dB.",
            color = Color(0xFF9BA3AF),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}