package com.example.ashasaathi

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import com.example.ashasaathi.ui.theme.BackgroundLight
import com.example.ashasaathi.ui.theme.PrimaryGreen

@Composable
fun SyncScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().background(BackgroundLight).padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Default.CheckCircle, 
            contentDescription = null, 
            tint = PrimaryGreen, 
            modifier = Modifier.size(80.dp)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text("Auto-Sync is Active", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "The app will automatically upload patient data as soon as internet is available.",
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(48.dp))
        Button(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
        ) { 
            Text("Back to Dashboard") 
        }

        Spacer(modifier = Modifier.height(32.dp))
        Text("Sent Alerts History", modifier = Modifier.align(Alignment.Start), fontWeight = FontWeight.Bold, color = PrimaryGreen)
        Spacer(modifier = Modifier.height(8.dp))
        
        val alerts by viewModel.alerts.collectAsState()
        val groupedAlerts = alerts.groupBy { it.patientName }
        
        if (alerts.isEmpty()) {
            Text("No alerts sent yet.", color = Color.Gray, modifier = Modifier.padding(16.dp))
        } else {
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(groupedAlerts.keys.toList()) { patientName ->
                    val patientAlerts = groupedAlerts[patientName] ?: emptyList()
                    // Filter to keep only the latest alert for each risk type (BP, Hb, Fever)
                    val uniqueRisks = patientAlerts
                        .distinctBy { it.riskFactor.substringBefore(":") } 
                        .joinToString(", ") { it.riskFactor }
                    
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(1.dp)
                    ) {
                        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Warning, contentDescription = null, tint = Color.Red, modifier = Modifier.size(24.dp))
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(patientName, fontWeight = FontWeight.Bold)
                                Text(uniqueRisks, fontSize = 12.sp, color = Color.Gray)
                            }
                        }
                    }
                }
            }
        }
    }
}
