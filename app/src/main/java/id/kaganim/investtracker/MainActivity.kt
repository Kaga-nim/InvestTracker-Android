package id.kaganim.investtracker

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import java.text.NumberFormat
import java.util.Locale
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.*
import kotlin.math.pow
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Calendar

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MainScreen()
        }
    }
}

@Composable
fun MainScreen() {
    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 20.dp)
            ) {
                Text(
                    text = "Investment Tracker",
                    style = MaterialTheme.typography.headlineMedium
                )

                Text(
                    text = "Simulasi pertumbuhan investasimu",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            Spacer(modifier = Modifier.height(8.dp))

            InputSection()

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun InputSection() {
    var modal by remember { mutableStateOf("") }
    var persen by remember { mutableStateOf("") }

    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    var startDate by remember { mutableStateOf("Pilih") }
    var endDate by remember { mutableStateOf("Pilih") }

    var chartData by remember { mutableStateOf(listOf<Double>()) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Text("Simulasi Investasi", style = MaterialTheme.typography.titleMedium)

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = modal,
                onValueChange = { modal = it },
                label = { Text("Modal Awal") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = persen,
                onValueChange = { persen = it },
                label = { Text("Return (%) / bulan") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {

                Button(onClick = {
                    DatePickerDialog(
                        context,
                        { _, y, m, d ->
                            startDate = "$d/${m + 1}/$y"
                        },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                    ).show()
                }) {
                    Text("Mulai\n$startDate")
                }

                Button(onClick = {
                    DatePickerDialog(
                        context,
                        { _, y, m, d ->
                            endDate = "$d/${m + 1}/$y"
                        },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                    ).show()
                }) {
                    Text("Selesai\n$endDate")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {

                    val modalDouble = modal.toDoubleOrNull()
                    val persenDouble = persen.toDoubleOrNull()

                    if (modalDouble == null || persenDouble == null) return@Button
                    if (startDate == "Pilih" || endDate == "Pilih") return@Button

                    val hari = hitungHari(startDate, endDate)

                    chartData = hitungInvestasiHarian(
                        modalDouble,
                        persenDouble,
                        hari
                    )
                }
            ) {
                Text("Hitung Investasi")
            }
        }
    }

    Spacer(modifier = Modifier.height(20.dp))

    if (chartData.isNotEmpty()) {

        val finalValue = chartData.last()

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.large
        ) {
            Column(modifier = Modifier.padding(16.dp)) {

                Text("Hasil Akhir", style = MaterialTheme.typography.titleMedium)

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    formatRupiah(finalValue),
                    style = MaterialTheme.typography.headlineSmall
                )

                Spacer(modifier = Modifier.height(16.dp))

                AndroidView(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    factory = { context ->
                        LineChart(context)
                    },
                    update = { chart ->

                        val entries = chartData.mapIndexed { i, v ->
                            Entry(i.toFloat(), v.toFloat())
                        }

                        val dataSet = LineDataSet(entries, "Growth").apply {
                            lineWidth = 3f
                            setDrawCircles(true)
                            setDrawValues(false)
                            mode = LineDataSet.Mode.CUBIC_BEZIER // smooth curve
                        }

                        chart.apply {
                            data = LineData(dataSet)
                            description.isEnabled = false
                            axisRight.isEnabled = false
                            xAxis.granularity = 1f
                            invalidate()
                        }
                    }
                )
            }
        }
    }
}

fun hitungHari(start: String, end: String): Int {
    val formatter = DateTimeFormatter.ofPattern("d/M/yyyy")

    val s = LocalDate.parse(start, formatter)
    val e = LocalDate.parse(end, formatter)

    val days = ChronoUnit.DAYS.between(s, e).toInt()

    return if (days <= 0) 1 else days
}

fun hitungInvestasiHarian(
    modal: Double,
    persenBulanan: Double,
    hari: Int
): List<Double> {

    val hasil = mutableListOf<Double>()
    var current = modal

    hasil.add(current)

    val dailyRate = (1 + (persenBulanan / 100)).pow(1.0 / 30.0) - 1

    repeat(hari) {
        current += current * dailyRate
        hasil.add(current)
    }

    return hasil
}

fun formatRupiah(value: Double): String {
    val formatter = NumberFormat.getCurrencyInstance(Locale("id", "ID")).apply {
        maximumFractionDigits = 0
    }
    return formatter.format(value)
}