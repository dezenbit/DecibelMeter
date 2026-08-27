package com.example.decibelmeter.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.max
import kotlin.math.min

@Composable
fun DecibelGraph(
    values: List<Float>,
    color: Color
) {

    Surface(
        modifier =
            Modifier
                .fillMaxWidth()
                .height(180.dp),
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

                if (values.size < 2) {
                    return@Canvas
                }

                val maxDb = 120f
                val minDb = 0f

                val step =
                    size.width /
                            (values.size - 1)

                /*
                 * Convertir los valores
                 * de dB a coordenadas Y.
                 */
                fun getY(value: Float): Float {

                    val normalized =
                        (
                                value - minDb
                                ) /
                                (maxDb - minDb)

                    return size.height -
                            (
                                    normalized
                                        .coerceIn(
                                            0f,
                                            1f
                                        ) *
                                            size.height
                                    )
                }

                /*
                 * Crear una línea suavizada
                 * utilizando puntos intermedios.
                 */
                val linePath = Path()

                val firstX = 0f
                val firstY =
                    getY(values.first())

                linePath.moveTo(
                    firstX,
                    firstY
                )

                for (
                i in 0 until values.size - 1
                ) {

                    val x1 =
                        i * step

                    val y1 =
                        getY(values[i])

                    val x2 =
                        (i + 1) * step

                    val y2 =
                        getY(values[i + 1])

                    val controlX =
                        (x1 + x2) / 2f

                    linePath.cubicTo(
                        controlX,
                        y1,
                        controlX,
                        y2,
                        x2,
                        y2
                    )
                }

                /*
                 * Área debajo de la línea.
                 */
                val areaPath =
                    Path()

                areaPath.moveTo(
                    0f,
                    size.height
                )

                areaPath.lineTo(
                    firstX,
                    firstY
                )

                for (
                i in 0 until values.size - 1
                ) {

                    val x1 =
                        i * step

                    val y1 =
                        getY(values[i])

                    val x2 =
                        (i + 1) * step

                    val y2 =
                        getY(values[i + 1])

                    val controlX =
                        (x1 + x2) / 2f

                    areaPath.cubicTo(
                        controlX,
                        y1,
                        controlX,
                        y2,
                        x2,
                        y2
                    )
                }

                areaPath.lineTo(
                    size.width,
                    size.height
                )

                areaPath.close()

                /*
                 * Dibujar área.
                 */
                drawPath(
                    path = areaPath,
                    color = color.copy(
                        alpha = 0.12f
                    )
                )

                /*
                 * Dibujar línea principal.
                 */
                drawPath(
                    path = linePath,
                    color = color,
                    style = Stroke(
                        width = 5.dp.toPx(),
                        cap = StrokeCap.Round,
                        join = StrokeJoin.Round
                    )
                )

                /*
                 * Dibujar el último punto.
                 */
                val lastIndex =
                    values.lastIndex

                val lastX =
                    lastIndex * step

                val lastY =
                    getY(values.last())

                drawCircle(
                    color = color,
                    radius = 6.dp.toPx(),
                    center =
                        Offset(
                            lastX,
                            lastY
                        )
                )
            }
        }
    }
}