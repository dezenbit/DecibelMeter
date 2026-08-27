package com.example.decibelmeter.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun SettingsScreen(
    onCalibrationClick: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {

        Text(
            text = "AJUSTES",
            color = Color.White,
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onCalibrationClick()
                },
            color = Color(0xFF161B22),
            shape = MaterialTheme.shapes.large
        ) {

            androidx.compose.foundation.layout.Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {

                Icon(
                    imageVector = Icons.Default.Tune,
                    contentDescription = "Calibración",
                    tint = Color.White
                )

                androidx.compose.foundation.layout.Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 16.dp)
                ) {

                    Text(
                        text = "Calibración",
                        color = Color.White,
                        style = MaterialTheme.typography.titleMedium
                    )

                    Text(
                        text = "Ajusta la lectura del micrófono",
                        color = Color(0xFF9BA3AF),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = "Abrir",
                    tint = Color(0xFF9BA3AF)
                )
            }
        }
    }
}