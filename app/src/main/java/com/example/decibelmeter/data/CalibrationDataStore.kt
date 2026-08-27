package com.example.decibelmeter.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(
    name = "decibel_meter_preferences"
)

class CalibrationDataStore(
    private val context: Context
) {

    companion object {

        private val CALIBRATION_OFFSET =
            floatPreferencesKey(
                "calibration_offset"
            )
    }

    val calibrationOffset: Flow<Float> =
        context.dataStore.data.map { preferences ->

            preferences[
                CALIBRATION_OFFSET
            ] ?: 0f
        }

    suspend fun saveCalibrationOffset(
        offset: Float
    ) {

        context.dataStore.edit { preferences ->

            preferences[
                CALIBRATION_OFFSET
            ] = offset
        }
    }
}
