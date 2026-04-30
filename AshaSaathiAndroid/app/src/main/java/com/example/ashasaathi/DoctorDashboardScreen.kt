package com.example.ashasaathi

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ashasaathi.ui.theme.*

import androidx.compose.runtime.LaunchedEffect

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoctorDashboardScreen(
    viewModel: MainViewModel,
    onNavigateToAlerts: () -> Unit,
    onNavigateToPatients: () -> Unit,
    onNavigateToReport: () -> Unit,
    onLogout: () -> Unit
) {
    val userName by viewModel.userName.collectAsState()
    val displayName = if (userName.isBlank()) "Doctor" else "Dr. $userName"

    LaunchedEffect(Unit) {
        viewModel.fetchPatients(isDoctor = true)
    }

    val report by viewModel.monthlyReport.collectAsState()
    val isSyncing by viewModel.isSyncing.collectAsState()
    val loginError by viewModel.loginError.collectAsState()
    
    LaunchedEffect(Unit) {
        viewModel.fetchPatients(isDoctor = true)
        // Pulse every 30 seconds to keep data fresh
        while(true) {
            kotlinx.coroutines.delay(30000)
            viewModel.fetchPatients(isDoctor = true)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Column {
                        Text("ASHA Saathi - Command Center", color = SurfaceWhite, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Text(
                            text = if (isSyncing) "Synchronizing..." else "Live Connection Active",
                            color = if (isSyncing) WarningOrange else PrimaryGreen,
                            fontSize = 12.sp
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.fetchPatients(isDoctor = true) }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh", tint = SurfaceWhite)
                    }
                    IconButton(onClick = onLogout) {
                        Icon(Icons.Default.ExitToApp, contentDescription = "Logout", tint = SurfaceWhite)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = SecondaryBlue)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(BackgroundLight)
                .padding(16.dp)
        ) {
            if (loginError != null) {
                Card(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                    colors = CardDefaults.cardColors(containerColor = AccentRed.copy(alpha = 0.1f))
                ) {
                    Text(
                        "Sync Issue: $loginError",
                        color = AccentRed,
                        modifier = Modifier.padding(16.dp),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Text(
                text = "Welcome, $displayName",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = SecondaryBlue
            )
            Text(
                text = "Triage Overview • ${report?.month ?: "Syncing..."}",
                fontSize = 14.sp,
                color = TextMuted,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                DashboardCard(
                    modifier = Modifier.weight(1f),
                    title = "Alerts Inbox",
                    value = report?.totalAlerts?.toString() ?: "0",
                    icon = Icons.Default.Warning,
                    iconColor = AccentRed,
                    onClick = onNavigateToAlerts
                )
                DashboardCard(
                    modifier = Modifier.weight(1f),
                    title = "Patient Directory",
                    value = report?.totalPatients?.toString() ?: "0",
                    icon = Icons.Default.Person,
                    iconColor = PrimaryGreen,
                    onClick = onNavigateToPatients
                )
            }
        }
    }
}

@Composable
fun ReportCard(title: String, value: String, icon: ImageVector, color: Color) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        elevation = CardDefaults.cardElevation(2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(color.copy(alpha = 0.1f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = color)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(title, color = TextMuted, fontSize = 14.sp)
                Text(value, color = TextDark, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun DashboardCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String = "",
    icon: ImageVector,
    iconColor: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .height(140.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        elevation = CardDefaults.cardElevation(2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(iconColor.copy(alpha = 0.1f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = iconColor)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(title, color = TextMuted, fontSize = 12.sp, fontWeight = FontWeight.Medium)
            if (value.isNotEmpty()) {
                Text(value, color = TextDark, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
