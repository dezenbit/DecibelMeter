package com.example.decibelmeter

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.core.content.ContextCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.decibelmeter.ui.CalibrationScreen
import com.example.decibelmeter.ui.DecibelMeterScreen
import com.example.decibelmeter.ui.SettingsScreen
import com.example.decibelmeter.ui.theme.DecibelMeterTheme
import com.example.decibelmeter.viewmodel.DecibelViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

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

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(savedInstanceState)

        setContent {

            DecibelMeterTheme {

                Surface {

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

    val navController =
        rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "meter"
    ) {

        /*
         * Pantalla principal
         */
        composable("meter") {

            DecibelMeterScreen(
                viewModel = viewModel,

                onSettingsClick = {
                    navController.navigate(
                        "settings"
                    )
                },

                onStart = onStart
            )
        }

        /*
         * Pantalla de ajustes
         */
        composable("settings") {

            SettingsScreen(
                onCalibrationClick = {

                    navController.navigate(
                        "calibration"
                    )
                }
            )
        }

        /*
         * Pantalla de calibración
         */
        composable("calibration") {

            val calibrationOffset by
            viewModel.calibrationOffset.collectAsState()

            CalibrationScreen(
                calibrationOffset =
                    calibrationOffset,

                onOffsetChange = { newOffset ->

                    viewModel.setCalibrationOffset(
                        newOffset
                    )
                },

                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}