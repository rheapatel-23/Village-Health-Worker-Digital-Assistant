package com.example.ashasaathi

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ashasaathi.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AshaDashboardScreen(
    viewModel: MainViewModel = viewModel(),
    onNavigateToRegistration: () -> Unit,
    onNavigateToSync: () -> Unit,
    onNavigateToPatientList: () -> Unit,
    onNavigateToReport: () -> Unit,
    onLogout: () -> Unit
) {
    val patients by viewModel.patients.collectAsState()
    val userName by viewModel.userName.collectAsState()
    val displayName = if (userName.isBlank()) "User" else userName
    val initial = displayName.firstOrNull()?.uppercase() ?: "U"

    LaunchedEffect(Unit) {
        viewModel.fetchPatients()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("ASHA Saathi", color = SurfaceWhite, fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PrimaryGreen),
                actions = {
                    IconButton(onClick = { /* Toggle Language */ }) {
                        Text("हिंदी", color = SurfaceWhite, fontWeight = FontWeight.Bold)
                    }
                    Box(
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(SecondaryBlue),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(initial, color = SurfaceWhite, fontWeight = FontWeight.Bold)
                    }
                    IconButton(onClick = onLogout) {
                        Icon(Icons.Default.ExitToApp, contentDescription = "Logout", tint = SurfaceWhite)
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = SurfaceWhite,
                tonalElevation = 8.dp
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.Home, contentDescription = "Home", tint = PrimaryGreen)
                    }
                    IconButton(onClick = onNavigateToPatientList) {
                        Icon(Icons.Default.Person, contentDescription = "Patients", tint = TextMuted)
                    }
                    IconButton(onClick = onNavigateToSync) {
                        Icon(Icons.Default.Refresh, contentDescription = "Sync", tint = TextMuted)
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(BackgroundLight)
                .padding(16.dp)
        ) {
            val loginError by viewModel.loginError.collectAsState()
            
            Text(
                text = "Namaste, $displayName",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = SecondaryBlue
            )
            Text(
                text = "Your village health overview for today.",
                fontSize = 14.sp,
                color = TextMuted,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            if (loginError != null) {
                Card(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                    colors = CardDefaults.cardColors(containerColor = AccentRed.copy(alpha = 0.1f))
                ) {
                    Text(
                        loginError!!,
                        color = AccentRed,
                        modifier = Modifier.padding(16.dp),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Grid
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                DashboardCard(
                    modifier = Modifier.weight(1f),
                    title = "Add Patient",
                    icon = Icons.Default.AddCircle,
                    iconColor = SecondaryBlue,
                    onClick = onNavigateToRegistration
                )
                DashboardCard(
                    modifier = Modifier.weight(1f),
                    title = "Pending Sync",
                    value = patients.count { it.status != "Synced" }.toString(),
                    valueColor = WarningOrange,
                    onClick = { 
                        viewModel.syncData()
                        onNavigateToSync() 
                    }
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                val alertCount by viewModel.alertCount.collectAsState()
                DashboardCard(
                    modifier = Modifier.weight(1f),
                    title = "Alert History",
                    value = alertCount.toString(),
                    valueColor = AccentRed,
                    onClick = { onNavigateToSync() }
                )
                DashboardCard(
                    modifier = Modifier.weight(1f),
                    title = "Total Patients",
                    value = patients.size.toString(),
                    valueColor = TextDark,
                    onClick = onNavigateToPatientList
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            DashboardCard(
                modifier = Modifier.fillMaxWidth().height(100.dp),
                title = "Monthly Government Reports",
                value = "View Stats & History",
                icon = Icons.Default.List,
                iconColor = SurfaceWhite,
                iconBgColor = SecondaryBlue,
                onClick = onNavigateToReport
            )

            Spacer(modifier = Modifier.height(24.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Recent Activity", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextDark)
                Text("View All", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = PrimaryGreen, modifier = Modifier.clickable { onNavigateToPatientList() })
            }
            Spacer(modifier = Modifier.height(8.dp))

            // Recent Activity List
            Card(
                modifier = Modifier.fillMaxWidth().weight(1f),
                colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                shape = RoundedCornerShape(12.dp)
            ) {
                val alerts by viewModel.alerts.collectAsState()
                val groupedAlerts = alerts.groupBy { it.patientName }
                
                if (patients.isEmpty() && alerts.isEmpty()) {
                    Column(
                        modifier = Modifier.fillMaxSize().padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(Icons.Default.Info, contentDescription = null, tint = TextMuted, modifier = Modifier.size(48.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("No recent activity found.", color = TextMuted)
                    }
                } else {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        // Alerts section (Grouped)
                        items(groupedAlerts.keys.toList().take(5)) { patientName ->
                            val patientAlerts = groupedAlerts[patientName] ?: emptyList()
                            val uniqueRisks = patientAlerts
                                .distinctBy { it.riskFactor.substringBefore(":") } 
                                .joinToString(", ") { it.riskFactor }
                            
                            ActivityItem(
                                title = "Critical: $patientName",
                                subtitle = uniqueRisks,
                                time = "Today",
                                icon = Icons.Default.Warning,
                                iconColor = AccentRed
                            )
                        }
                        // Patients section
                        items(patients.take(10)) { patient ->
                            ActivityItem(
                                title = "Registered: ${patient.name}",
                                subtitle = "Patient added to directory",
                                time = "Recently",
                                icon = Icons.Default.Person,
                                iconColor = PrimaryGreen
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DashboardCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String? = null,
    icon: androidx.compose.ui.graphics.vector.ImageVector? = null,
    iconColor: Color = TextDark,
    iconBgColor: Color = Color.Transparent,
    valueColor: Color = TextDark,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .height(100.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (icon != null) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .background(if(iconBgColor == Color.Transparent) iconColor.copy(alpha = 0.1f) else iconBgColor, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(icon, contentDescription = null, tint = if(iconBgColor == Color.Transparent) iconColor else SurfaceWhite, modifier = Modifier.size(24.dp))
                }
                Spacer(modifier = Modifier.height(4.dp))
            }
            if (value != null) {
                Text(text = value, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = valueColor)
            }
            Text(text = title, fontSize = 12.sp, color = TextMuted, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
fun ActivityItem(
    title: String,
    subtitle: String,
    time: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconColor: androidx.compose.ui.graphics.Color
) {
    ListItem(
        headlineContent = { Text(title, fontWeight = FontWeight.Bold, fontSize = 14.sp) },
        supportingContent = { Text(subtitle, fontSize = 12.sp, color = TextMuted) },
        trailingContent = { Text(time, fontSize = 11.sp, color = TextMuted) },
        leadingContent = { 
            Box(
                modifier = Modifier.size(40.dp).background(iconColor.copy(alpha = 0.1f), androidx.compose.foundation.shape.CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(20.dp))
            }
        },
        colors = ListItemDefaults.colors(containerColor = androidx.compose.ui.graphics.Color.Transparent)
    )
    Divider(modifier = Modifier.padding(horizontal = 16.dp), color = BackgroundLight)
}
