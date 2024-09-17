package com.example.my_application

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.my_application.ui.theme.MyApplicationTheme
import kotlin.math.pow

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                ResistanceCalculator()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResistanceCalculator() {
    val context = LocalContext.current

    var band1 by remember { mutableStateOf<String?>(null) }
    var band2 by remember { mutableStateOf<String?>(null) }
    var multiplier by remember { mutableStateOf<String?>(null) }
    var toleranceBand by remember { mutableStateOf<String?>(null) }
    var isExpanded1 by remember { mutableStateOf(false) }
    var isExpanded2 by remember { mutableStateOf(false) }
    var isExpanded3 by remember { mutableStateOf(false) }
    var isExpanded4 by remember { mutableStateOf(false) }

    val colorBandValues = mapOf(
        "Negro" to 0,
        "Marrón" to 1,
        "Rojo" to 2,
        "Naranja" to 3,
        "Amarillo" to 4,
        "Verde" to 5,
        "Azul" to 6,
        "Violeta" to 7,
        "Gris" to 8,
        "Blanco" to 9
    )

    val colorMap = mapOf(
        "Negro" to Color.Black,
        "Marrón" to Color(0xFFA52A2A),
        "Rojo" to Color.Red,
        "Naranja" to Color(0xFFFFA500),
        "Amarillo" to Color.Yellow,
        "Verde" to Color.Green,
        "Azul" to Color.Blue,
        "Violeta" to Color(0xFF8A2BE2),
        "Gris" to Color.Gray,
        "Blanco" to Color.White,
        "Dorado" to Color(0xFFFFD700),
        "Plata" to Color(0xFFC0C0C0)
    )

    val toleranceBandValues = mapOf(
        "Marrón" to 1.0,
        "Rojo" to 2.0,
        "Verde" to 0.5,
        "Azul" to 0.25,
        "Violeta" to 0.1,
        "Gris" to 0.05,
        "Dorado" to 5.0,
        "Plata" to 10.0
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            ExposedDropdownMenuBox(
                expanded = isExpanded1,
                onExpandedChange = { isExpanded1 = !isExpanded1 }
            ) {
                TextField(
                    value = band1 ?: "Banda 1",
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(isExpanded1) },
                    colors = ExposedDropdownMenuDefaults.textFieldColors(),
                    modifier = Modifier.menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = isExpanded1,
                    onDismissRequest = { isExpanded1 = false }
                ) {
                    colorBandValues.keys.forEach { color ->
                        DropdownMenuItem(
                            text = { Text(color) },
                            onClick = {
                                band1 = color
                                isExpanded1 = false
                            }
                        )
                    }
                }
            }

            ExposedDropdownMenuBox(
                expanded = isExpanded2,
                onExpandedChange = { isExpanded2 = !isExpanded2 }
            ) {
                TextField(
                    value = band2 ?: "Banda 2",
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(isExpanded2) },
                    colors = ExposedDropdownMenuDefaults.textFieldColors(),
                    modifier = Modifier.menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = isExpanded2,
                    onDismissRequest = { isExpanded2 = false }
                ) {
                    colorBandValues.keys.forEach { color ->
                        DropdownMenuItem(
                            text = { Text(color) },
                            onClick = {
                                band2 = color
                                isExpanded2 = false
                            }
                        )
                    }
                }
            }

            ExposedDropdownMenuBox(
                expanded = isExpanded3,
                onExpandedChange = { isExpanded3 = !isExpanded3 }
            ) {
                TextField(
                    value = multiplier ?: "Multiplicador",
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(isExpanded3) },
                    colors = ExposedDropdownMenuDefaults.textFieldColors(),
                    modifier = Modifier.menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = isExpanded3,
                    onDismissRequest = { isExpanded3 = false }
                ) {
                    colorBandValues.keys.forEach { color ->
                        DropdownMenuItem(
                            text = { Text(color) },
                            onClick = {
                                multiplier = color
                                isExpanded3 = false
                            }
                        )
                    }
                }
            }

            ExposedDropdownMenuBox(
                expanded = isExpanded4,
                onExpandedChange = { isExpanded4 = !isExpanded4 }
            ) {
                TextField(
                    value = toleranceBand ?: "Tolerancia",
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(isExpanded4) },
                    colors = ExposedDropdownMenuDefaults.textFieldColors(),
                    modifier = Modifier.menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = isExpanded4,
                    onDismissRequest = { isExpanded4 = false }
                ) {
                    toleranceBandValues.keys.forEach { color ->
                        DropdownMenuItem(
                            text = { Text(color) },
                            onClick = {
                                toleranceBand = color
                                isExpanded4 = false
                            }
                        )
                    }
                }
            }

            // Canvas to draw the resistor and bands
            Spacer(modifier = Modifier.height(20.dp))
            Canvas(modifier = Modifier
                .size(width = 300.dp, height = 100.dp)
                .background(Color.LightGray)) {
                val bandColors = listOf(
                    colorMap[band1] ?: Color.Transparent,
                    colorMap[band2] ?: Color.Transparent,
                    colorMap[multiplier] ?: Color.Transparent,
                    colorMap[toleranceBand] ?: Color.Transparent
                )

                // Draw resistor body
                drawRoundRect(
                    color = Color.DarkGray,
                    size = size.copy(width = 260.dp.toPx(), height = 60.dp.toPx()),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(10.dp.toPx())
                )

                // Draw color bands
                val bandWidth = 15.dp.toPx()
                val startX = (size.width - 260.dp.toPx()) / 2
                bandColors.forEachIndexed { index, color ->
                    drawLine(
                        color = color,
                        start = androidx.compose.ui.geometry.Offset(startX + index * 50.dp.toPx(), 20.dp.toPx()),
                        end = androidx.compose.ui.geometry.Offset(startX + index * 50.dp.toPx(), size.height - 20.dp.toPx()),
                        strokeWidth = bandWidth,
                        cap = StrokeCap.Square
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(onClick = {
                // Obtener los valores de las bandas de color
                val value1 = colorBandValues[band1] ?: 0
                val value2 = colorBandValues[band2] ?: 0
                val multiplierValue = colorBandValues[multiplier]?.let { 10.0.pow(it).toInt() } ?: 1
                val toleranceValue = toleranceBandValues[toleranceBand] ?: 0.0

                // Calcular el valor nominal de la resistencia
                val resistanceValue = (value1 * 10 + value2) * multiplierValue

                // Calcular el rango de tolerancia
                val toleranceAbsolute = resistanceValue * (toleranceValue / 100)
                val minResistance = resistanceValue - toleranceAbsolute
                val maxResistance = resistanceValue + toleranceAbsolute

                // Mostrar el resultado en un Toast
                Toast.makeText(
                    context,
                    "Valor de la resistencia: $resistanceValue Ω\n" +
                            "Tolerancia: ±$toleranceValue%\n" +
                            "Rango: $minResistance Ω - $maxResistance Ω",
                    Toast.LENGTH_LONG
                ).show()
            }) {
                Text("Calcular")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewResistanceCalculator() {
    MyApplicationTheme {
        ResistanceCalculator()
    }
}
