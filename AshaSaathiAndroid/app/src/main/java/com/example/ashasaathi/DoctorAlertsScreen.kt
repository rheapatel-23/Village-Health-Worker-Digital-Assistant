package com.example.ashasaathi

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

import androidx.compose.runtime.LaunchedEffect

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoctorAlertsScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit
) {
    val alerts by viewModel.alerts.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchAlerts()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Critical Alerts Inbox", color = SurfaceWhite) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = AccentRed)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues).background(BackgroundLight).padding(16.dp)
        ) {
            if (alerts.isEmpty()) {
                Column(
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(Icons.Default.Warning, contentDescription = null, tint = TextMuted, modifier = Modifier.size(48.dp))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("No pending alerts.", color = TextMuted)
                }
            } else {
                val groupedAlerts = alerts.groupBy { it.patientName }
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(groupedAlerts.keys.toList()) { patientName ->
                        val patientAlerts = groupedAlerts[patientName] ?: emptyList()
                        // Filter to keep only the latest alert for each risk type
                        val uniqueRisks = patientAlerts
                            .distinctBy { it.riskFactor.substringBefore(":") } 
                            .joinToString(", ") { it.riskFactor }
                        
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                            elevation = CardDefaults.cardElevation(2.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.Warning, contentDescription = null, tint = AccentRed, modifier = Modifier.size(32.dp))
                                Spacer(modifier = Modifier.width(16.dp))
                                Column {
                                    Text(patientName, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                                    Text("Risks: $uniqueRisks", color = AccentRed, fontWeight = FontWeight.SemiBold)
                                    Text("Received: Just now", color = TextMuted, fontSize = 12.sp)
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
