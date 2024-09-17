package com.example.my_application

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.my_application.ui.theme.MyApplicationTheme
import kotlin.math.pow

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
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
    var toleranceBand by remember { mutableStateOf<String?>(null) } // Nueva variable para la tolerancia
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
    val toleranceBandValues = mapOf(
        "Marrón" to 1.0,  // 1% de tolerancia
        "Rojo" to 2.0,    // 2% de tolerancia
        "Verde" to 0.5,   // 0.5% de tolerancia
        "Azul" to 0.25,   // 0.25% de tolerancia
        "Violeta" to 0.1, // 0.1% de tolerancia
        "Gris" to 0.05,   // 0.05% de tolerancia
        "Dorado" to 5.0,  // 5% de tolerancia
        "Plata" to 10.0   // 10% de tolerancia
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        contentAlignment = Alignment.Center
    ) {
        Column {
            // Banda 1
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

            // Banda 2
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

            // Multiplicador
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

            // Banda de tolerancia
            ExposedDropdownMenuBox(
                expanded = isExpanded4,
                onExpandedChange = { isExpanded4 = !isExpanded4 }
            ) {
                TextField(
                    value = toleranceBand ?: "Selecciona Tolerancia",
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

            // Botón Calcular
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
fun DefaultPreview() {
    MyApplicationTheme {
        ResistanceCalculator()
    }
}
