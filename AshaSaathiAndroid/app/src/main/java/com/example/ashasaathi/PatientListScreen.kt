package com.example.ashasaathi

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ashasaathi.ui.theme.BackgroundLight

import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.ashasaathi.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientListScreen(
    viewModel: MainViewModel, 
    onNavigateToDetails: (Int) -> Unit,
    onBack: () -> Unit
) {
    val patients by viewModel.patients.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Patient Directory", color = SurfaceWhite) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PrimaryGreen)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues).background(BackgroundLight)
        ) {
            if (patients.isEmpty()) {
                Column(
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("No patients added yet.", color = TextMuted)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f).padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    itemsIndexed(patients) { index, patient ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                            elevation = CardDefaults.cardElevation(2.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("#${index + 1} ${patient.name}", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = TextDark)
                                    Text(
                                        patient.status, 
                                        color = if (patient.status == "Synced") PrimaryGreen else WarningOrange,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp
                                    )
                                }
                                Text("Age: ${patient.type} • ASHA: ${patient.ashaName}", color = TextMuted, fontSize = 14.sp)
                                Divider(modifier = Modifier.padding(vertical = 8.dp), color = BackgroundLight)
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Status: Stable", color = SecondaryBlue, fontWeight = FontWeight.Bold)
                                    Text(
                                        "Health Record >", 
                                        color = PrimaryGreen, 
                                        fontSize = 14.sp,
                                        modifier = Modifier.clickable { onNavigateToDetails(patient.id.toInt()) }
                                    )
                                }
                            }
                        }
                    }
                }
            }
            
            Button(
                onClick = onBack, 
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
            ) { 
                Text("Back to Dashboard") 
            }
        }
    }
}
