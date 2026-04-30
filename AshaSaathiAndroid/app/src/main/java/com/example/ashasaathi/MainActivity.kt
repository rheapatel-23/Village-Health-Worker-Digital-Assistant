package com.example.ashasaathi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Alignment
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ashasaathi.ui.theme.AshaSaathiTheme

import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AshaSaathiTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val viewModel: MainViewModel = viewModel()
    
    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(
                viewModel = viewModel,
                onLoginSuccess = { isAsha ->
                    val dest = if (isAsha) "dashboard" else "doctorDashboard"
                    navController.navigate(dest) {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }
        composable("dashboard") {
            AshaDashboardScreen(
                viewModel = viewModel,
                onNavigateToRegistration = { navController.navigate("registration") },
                onNavigateToSync = { navController.navigate("sync") },
                onNavigateToPatientList = { navController.navigate("patientList") },
                onNavigateToReport = { navController.navigate("report") },
                onLogout = { navController.navigate("login") { popUpTo(0) } }
            )
        }
        composable("doctorDashboard") {
            DoctorDashboardScreen(
                viewModel = viewModel,
                onNavigateToAlerts = { navController.navigate("doctorAlerts") },
                onNavigateToPatients = { navController.navigate("doctorPatients") },
                onNavigateToReport = { }, // Disabled for Doctor
                onLogout = { navController.navigate("login") { popUpTo(0) } }
            )
        }
        composable("doctorAlerts") {
            DoctorAlertsScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
        composable("doctorPatients") {
            DoctorPatientListScreen(
                viewModel = viewModel,
                onNavigateToDetails = { id -> navController.navigate("patientDetail/$id") },
                onBack = { navController.popBackStack() }
            )
        }
        composable("registration") {
            PatientRegistrationScreen(
                viewModel = viewModel,
                onNext = { navController.navigate("healthCheck") },
                onBack = { navController.popBackStack() }
            )
        }
        composable("healthCheck") {
            HealthCheckScreen(
                viewModel = viewModel,
                onSave = { navController.popBackStack("dashboard", false) },
                onCriticalRisk = { navController.navigate("voiceAlert") },
                onBack = { navController.popBackStack() }
            )
        }
        composable("voiceAlert") {
            VoiceAlertScreen(
                onAlertSent = { navController.popBackStack("dashboard", false) },
                onDiscard = { navController.popBackStack() }
            )
        }
        composable("sync") {
            SyncScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
        composable("patientList") {
            PatientListScreen(
                viewModel = viewModel,
                onNavigateToDetails = { id -> navController.navigate("patientDetail/$id") },
                onBack = { navController.popBackStack() }
            )
        }
        composable("patientDetail/{patientId}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("patientId")?.toIntOrNull() ?: 0
            PatientDetailScreen(
                patientId = id,
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
        composable("report") {
            OfficialReportScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
