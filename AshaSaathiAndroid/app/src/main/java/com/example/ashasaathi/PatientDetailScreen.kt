package com.example.ashasaathi

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ashasaathi.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientDetailScreen(
    patientId: Int,
    viewModel: MainViewModel,
    onBack: () -> Unit
) {
    var patientEntity by remember { mutableStateOf<PatientEntity?>(null) }

    LaunchedEffect(patientId) {
        patientEntity = viewModel.getPatientEntityById(patientId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Health Record Details", color = SurfaceWhite) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = SurfaceWhite)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PrimaryGreen)
            )
        }
    ) { paddingValues ->
        if (patientEntity == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = PrimaryGreen)
            }
        } else {
            val p = patientEntity!!
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(BackgroundLight)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                // Header Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = androidx.compose.foundation.shape.CircleShape,
                            color = PrimaryGreen.copy(alpha = 0.1f),
                            modifier = Modifier.size(60.dp)
                        ) {
                            Icon(
                                Icons.Default.Person, 
                                contentDescription = null, 
                                tint = PrimaryGreen, 
                                modifier = Modifier.padding(12.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        val patients by viewModel.patients.collectAsState()
                        val displayId = patients.indexOfFirst { it.id.toInt() == p.id } + 1
                        Column {
                            Text(p.name, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = TextDark)
                            Text("Applicant ID: #$displayId • ${if(p.isSynced) "Synced" else "Offline"}", color = if(p.isSynced) PrimaryGreen else WarningOrange, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                DetailSection(title = "Personal Information") {
                    DetailRow(label = "Age", value = "${p.age} years")
                    DetailRow(label = "Gender", value = p.gender)
                    DetailRow(label = "Address", value = p.address)
                    DetailRow(label = "ASHA Worker", value = p.ashaName)
                }

                Spacer(modifier = Modifier.height(16.dp))

                DetailSection(title = "Vital Signs") {
                    DetailRow(label = "Blood Pressure", value = "${p.systolicBP}/${p.diastolicBP} mmHg", valueColor = if(p.systolicBP > 140) AccentRed else TextDark)
                    DetailRow(label = "Temperature", value = p.temperature)
                    DetailRow(label = "Hemoglobin (Hb)", value = p.hemoglobin)
                }

                Spacer(modifier = Modifier.height(16.dp))

                DetailSection(title = "Maternal & Vaccination") {
                    DetailRow(label = "Maternal Status", value = p.pregnancyStatus)
                    DetailRow(label = "Vaccine Remarks", value = p.vaccinationRemarks)
                }

                Spacer(modifier = Modifier.height(16.dp))

                DetailSection(title = "Reported Symptoms") {
                    Text(
                        p.reportedSymptoms, 
                        color = TextDark, 
                        modifier = Modifier.padding(vertical = 8.dp),
                        lineHeight = 20.sp
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))
                
                Button(
                    onClick = { viewModel.deletePatient(p.id); onBack() },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent, contentColor = AccentRed)
                ) {
                    Text("Delete This Record")
                }
            }
        }
    }
}

@Composable
fun DetailSection(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column {
        Text(title, color = PrimaryGreen, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
            elevation = CardDefaults.cardElevation(1.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                content()
            }
        }
    }
}

@Composable
fun DetailRow(label: String, value: String, valueColor: Color = TextDark) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = TextMuted, fontSize = 14.sp)
        Text(value, color = valueColor, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
    }
}
