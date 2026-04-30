package com.example.ashasaathi

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ashasaathi.ui.theme.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoctorPatientListScreen(
    viewModel: MainViewModel,
    onNavigateToDetails: (Int) -> Unit,
    onBack: () -> Unit
) {
    val patients by viewModel.patients.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Patient Triage Directory", color = SurfaceWhite) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = SecondaryBlue)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues).background(BackgroundLight).padding(16.dp)
        ) {
            if (patients.isEmpty()) {
                Column(
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(Icons.Default.Info, contentDescription = null, tint = TextMuted, modifier = Modifier.size(48.dp))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("No patients synced yet.", color = TextMuted)
                }
            } else {
                LazyColumn(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    itemsIndexed(patients) { index, patient ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onNavigateToDetails(patient.id.toInt()) },
                            colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                            elevation = CardDefaults.cardElevation(2.dp)
                        ) {
                            Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier.size(40.dp).background(PrimaryGreen.copy(alpha = 0.1f), androidx.compose.foundation.shape.CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text((index + 1).toString(), fontWeight = FontWeight.Bold, color = PrimaryGreen)
                                }
                                Spacer(modifier = Modifier.width(16.dp))
                                Column {
                                    Text("#${index + 1} ${patient.name}", fontWeight = FontWeight.Bold)
                                    Text("ASHA: ${patient.ashaName} • Updated: ${patient.lastUpdated}", fontSize = 12.sp, color = TextMuted)
                                }
                            }
                        }
                    }
                }
            }
            Button(onClick = onBack, modifier = Modifier.fillMaxWidth()) { Text("Back to Dashboard") }
        }
    }
}
