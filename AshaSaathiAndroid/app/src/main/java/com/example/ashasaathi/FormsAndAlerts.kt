package com.example.ashasaathi

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ashasaathi.ui.theme.BackgroundLight
import com.example.ashasaathi.ui.theme.PrimaryGreen
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun PatientRegistrationScreen(
    viewModel: MainViewModel,
    onNext: () -> Unit,
    onBack: () -> Unit
) {
    var name by viewModel.regName
    var age by viewModel.regAge
    var sex by viewModel.regGender
    var address by viewModel.regAddress

    Column(
        modifier = Modifier.fillMaxSize().background(BackgroundLight).padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Patient Info", style = MaterialTheme.typography.headlineMedium, color = PrimaryGreen, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(24.dp))
        
        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Full Name") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(16.dp))
        
        OutlinedTextField(value = age, onValueChange = { age = it }, label = { Text("Age") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(16.dp))
        
        OutlinedTextField(value = sex, onValueChange = { sex = it }, label = { Text("Sex (M/F)") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(value = address, onValueChange = { address = it }, label = { Text("Address / Village Name") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(32.dp))
        
        Button(
            onClick = onNext, 
            modifier = Modifier.fillMaxWidth().height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
        ) { 
            Text("Next: Vitals & Health Check", fontSize = 16.sp) 
        }
        Spacer(modifier = Modifier.height(8.dp))
        TextButton(onClick = onBack) { Text("Back to Dashboard", color = PrimaryGreen) }
    }
}

@Composable
fun HealthCheckScreen(
    viewModel: MainViewModel,
    onSave: () -> Unit,
    onCriticalRisk: () -> Unit,
    onBack: () -> Unit
) {
    var bp by remember { mutableStateOf("") }
    var temp by remember { mutableStateOf("") }
    var hb by remember { mutableStateOf("") }
    var maternalStatus by remember { mutableStateOf("") }
    var vaccineStatus by remember { mutableStateOf("") }
    var symptoms by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        Text("Health & Vitals", style = MaterialTheme.typography.headlineMedium, color = PrimaryGreen, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(24.dp))
        
        Text("Vitals (BP/Temp/Hb)", modifier = Modifier.align(Alignment.Start), fontWeight = FontWeight.Bold, color = PrimaryGreen)
        OutlinedTextField(value = bp, onValueChange = { bp = it }, label = { Text("Blood Pressure (e.g. 120/80)") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = temp, onValueChange = { temp = it }, label = { Text("Temperature (°F)") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = hb, onValueChange = { hb = it }, label = { Text("Hemoglobin (Hb)") }, modifier = Modifier.fillMaxWidth())
        
        Spacer(modifier = Modifier.height(24.dp))
        Text("Maternal / Child / Vaccines", modifier = Modifier.align(Alignment.Start), fontWeight = FontWeight.Bold, color = PrimaryGreen)
        OutlinedTextField(value = maternalStatus, onValueChange = { maternalStatus = it }, label = { Text("Pregnancy/Child Status (e.g. MONTH 5)") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = vaccineStatus, onValueChange = { vaccineStatus = it }, label = { Text("Vaccine Remarks (e.g. TT-1 Done)") }, modifier = Modifier.fillMaxWidth())
        
        Spacer(modifier = Modifier.height(24.dp))
        Text("Reported Symptoms", modifier = Modifier.align(Alignment.Start), fontWeight = FontWeight.Bold, color = PrimaryGreen)
        OutlinedTextField(value = symptoms, onValueChange = { symptoms = it }, label = { Text("Symptoms (e.g. Mild Fatigue)") }, modifier = Modifier.fillMaxWidth(), minLines = 2)

        Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = { 
                    val systolic = bp.split("/").firstOrNull()?.toIntOrNull() ?: 120
                    val diastolic = bp.split("/").lastOrNull()?.toIntOrNull() ?: 80
                    val hbValue = hb.toDoubleOrNull() ?: 12.0
                    val tempValue = temp.replace("F", "").replace("°", "").toDoubleOrNull() ?: 98.6

                    viewModel.savePatientLocally(
                        name = viewModel.regName.value,
                        age = viewModel.regAge.value.toIntOrNull() ?: 0,
                        gender = viewModel.regGender.value,
                        address = viewModel.regAddress.value,
                        bp = bp,
                        temp = temp,
                        hb = hb,
                        maternal = maternalStatus,
                        vaccine = vaccineStatus,
                        symptoms = symptoms
                    )
                    
                    // Comprehensive Alert Logic
                    if (systolic >= 140 || diastolic >= 90) {
                        viewModel.incrementAlertCount(viewModel.regName.value, "High BP: $bp")
                    }
                    if (hbValue < 11.0) {
                        viewModel.incrementAlertCount(viewModel.regName.value, "Low Hb: $hbValue")
                    }
                    if (tempValue > 100.4) {
                        viewModel.incrementAlertCount(viewModel.regName.value, "High Fever: $tempValue°F")
                    }
                    
                    // Clear state
                    viewModel.regName.value = ""
                    viewModel.regAge.value = ""
                    viewModel.regGender.value = ""
                    viewModel.regAddress.value = ""
                    
                    onSave() 
                }, 
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen), 
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) { Text("Save Health Record", fontSize = 16.sp) }
        Spacer(modifier = Modifier.height(8.dp))
        TextButton(onClick = onBack) { Text("Back", color = PrimaryGreen) }
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun VoiceAlertScreen(
    onAlertSent: () -> Unit,
    onDiscard: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().background(BackgroundLight).padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Voice Alert", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = { /* Simulate recording toggle */ }, modifier = Modifier.size(80.dp)) {
            Text("MIC")
        }
        Spacer(modifier = Modifier.height(32.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            OutlinedButton(onClick = onDiscard) { Text("Discard") }
            Button(onClick = onAlertSent) { Text("Send Alert") }
        }
    }
}
