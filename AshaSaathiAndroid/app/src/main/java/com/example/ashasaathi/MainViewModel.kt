package com.example.ashasaathi

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

// Data Models (Ready for Backend Integration)
data class Patient(
    val id: String,
    val name: String,
    val type: String,
    val status: String,
    val lastUpdated: String,
    val ashaName: String
)

data class Alert(
    val id: String,
    val patientName: String,
    val riskFactor: String,
    val timestamp: String
)

data class MonthlyReport(
    val month: String,
    val totalPatients: Int,
    val totalAlerts: Int,
    val highBPCases: Int
)

class MainViewModel(application: android.app.Application) : androidx.lifecycle.AndroidViewModel(application) {
    
    private val patientDao = AppDatabase.getDatabase(application).patientDao()
    
    // State flows representing the UI state
    private val _patients = MutableStateFlow<List<Patient>>(emptyList())
    val patients: StateFlow<List<Patient>> = _patients.asStateFlow()

    // Temporary state for registration
    var regName = androidx.compose.runtime.mutableStateOf("")
    var regAge = androidx.compose.runtime.mutableStateOf("")
    var regGender = androidx.compose.runtime.mutableStateOf("")
    var regAddress = androidx.compose.runtime.mutableStateOf("")

    private val _alerts = MutableStateFlow<List<Alert>>(emptyList())
    val alerts: StateFlow<List<Alert>> = _alerts.asStateFlow()

    private val _isSyncing = MutableStateFlow(false)
    val isSyncing: StateFlow<Boolean> = _isSyncing.asStateFlow()

    private val _userName = MutableStateFlow("")
    val userName: StateFlow<String> = _userName.asStateFlow()

    private val _loginError = MutableStateFlow<String?>(null)
    val loginError: StateFlow<String?> = _loginError.asStateFlow()

    private val _alertCount = MutableStateFlow(0)
    val alertCount: StateFlow<Int> = _alertCount.asStateFlow()

    private val _monthlyReport = MutableStateFlow<MonthlyReport?>(null)
    val monthlyReport: StateFlow<MonthlyReport?> = _monthlyReport.asStateFlow()

    fun fetchMonthlyReport() {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.getMonthlyReport()
                _monthlyReport.value = MonthlyReport(
                    month = response.month,
                    totalPatients = response.summary.totalPatients,
                    totalAlerts = response.summary.totalAlerts,
                    highBPCases = response.summary.highBPCases
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    init {
        // Observe local database and update UI automatically
        viewModelScope.launch {
            patientDao.getAllPatientsFlow().collect { localList ->
                _patients.value = localList.map { 
                    Patient(
                        id = it.id.toString(), 
                        name = it.name, 
                        type = "Age: ${it.age}", 
                        status = if (it.isSynced) "Synced" else "Waiting for Internet", 
                        lastUpdated = "Just now",
                        ashaName = it.ashaName
                    )
                }
            }
        }

        viewModelScope.launch {
            patientDao.getAllAlertsFlow().collect { localAlerts ->
                val displayAlerts = localAlerts.map { 
                    Alert(it.id.toString(), it.patientName, it.riskFactor, "Just now")
                }.distinctBy { "${it.patientName}-${it.riskFactor.substringBefore(":")}" }
                _alerts.value = displayAlerts
                _alertCount.value = displayAlerts.size
            }
        }
        
        // Safety check: Observe connectivity but don't crash if permission is weirdly handled
        try {
            observeConnectivity()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        startPeriodicSync()
    }

    private fun observeConnectivity() {
        val connectivityManager = getApplication<android.app.Application>()
            .getSystemService(android.content.Context.CONNECTIVITY_SERVICE) as android.net.ConnectivityManager
        
        val networkRequest = android.net.NetworkRequest.Builder()
            .addCapability(android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
            
        connectivityManager.registerNetworkCallback(networkRequest, object : android.net.ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: android.net.Network) {
                // When internet is back, sync everything immediately
                syncData()
            }
        })
    }

    private fun startPeriodicSync() {
        val constraints = androidx.work.Constraints.Builder()
            .setRequiredNetworkType(androidx.work.NetworkType.CONNECTED)
            .build()

        val periodicSyncRequest = androidx.work.PeriodicWorkRequestBuilder<SyncWorker>(15, java.util.concurrent.TimeUnit.MINUTES)
            .setConstraints(constraints)
            .build()

        androidx.work.WorkManager.getInstance(getApplication())
            .enqueueUniquePeriodicWork(
                "periodic_sync",
                androidx.work.ExistingPeriodicWorkPolicy.KEEP,
                periodicSyncRequest
            )
    }

    fun setUserName(name: String) {
        _userName.value = name
    }

    fun incrementAlertCount(patientName: String, reason: String) {
        viewModelScope.launch {
            patientDao.insertAlert(AlertEntity(patientName = patientName, riskFactor = reason))
        }
    }

    fun login(email: String, password: String, isAsha: Boolean, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                val role = if (isAsha) "asha" else "doctor"
                val response = RetrofitClient.apiService.login(LoginRequest(email, password, role))
                
                // If we get here, it's a 200 OK success
                _userName.value = response.user?.email?.substringBefore("@")?.replaceFirstChar { it.uppercase() } ?: "User"
                _loginError.value = null
                onSuccess()
            } catch (e: retrofit2.HttpException) {
                // This catches 401 Unauthorized or other server errors
                if (e.code() == 401) {
                    _loginError.value = "Invalid Email or Password"
                } else {
                    _loginError.value = "Server Error: ${e.code()}"
                }
            } catch (e: Exception) {
                // This catches actual connection failures and surfaces useful diagnostics.
                val reason = e.localizedMessage?.takeIf { it.isNotBlank() } ?: "Unable to reach backend"
                _loginError.value = "Connection Error (${RetrofitClient.BASE_URL}): $reason"
                e.printStackTrace()
            }
        }
    }

    fun fetchPatients(isDoctor: Boolean = false) {
        if (!isDoctor) {
            refreshLocalPatients()
            return
        }
        
        viewModelScope.launch {
            _isSyncing.value = true
            try {
                val response = RetrofitClient.apiService.getPatients()
                _patients.value = response.data.map { 
                    Patient(
                        id = it.id.toString(), 
                        name = it.name, 
                        type = "Age: ${it.age}", 
                        status = "Synced", 
                        lastUpdated = it.timestamp,
                        ashaName = "ASHA Worker"
                    )
                }
                // Automatically update report statistics whenever patients are refreshed
                fetchMonthlyReport()
                fetchAlerts()
                _loginError.value = null
            } catch (e: Exception) {
                val reason = e.localizedMessage?.takeIf { it.isNotBlank() } ?: "Unable to reach backend"
                _loginError.value = "Connection Error (${RetrofitClient.BASE_URL}): $reason"
                e.printStackTrace()
            } finally {
                _isSyncing.value = false
            }
        }
    }

    fun fetchAlerts() {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.getAlerts()
                _alerts.value = response.data.map { 
                    Alert(it.id.toString(), it.patientName, it.riskFactor, it.timestamp)
                }
                _alertCount.value = _alerts.value.size
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun refreshLocalPatients() {
        viewModelScope.launch {
            val localList = patientDao.getAllPatients()
            _patients.value = localList.map { 
                Patient(
                    id = it.id.toString(), 
                    name = it.name, 
                    type = "Age: ${it.age}", 
                    status = if (it.isSynced) "Synced" else "Waiting for Internet", 
                    lastUpdated = "Just now",
                    ashaName = it.ashaName
                )
            }
        }
    }

    fun clearDatabase() {
        viewModelScope.launch {
            patientDao.deleteAll()
        }
    }

    fun deletePatient(id: Int) {
        viewModelScope.launch {
            val patient = patientDao.getPatientById(id)
            if (patient != null) {
                // Delete patient
                patientDao.deletePatient(id)
                // Delete associated alerts for this patient name to clean up the dashboard
                patientDao.deleteAlertsForPatient(patient.name)
            }
        }
    }

    suspend fun getPatientEntityById(id: Int): PatientEntity? {
        return patientDao.getPatientById(id)
    }

    fun savePatientLocally(
        name: String, 
        age: Int, 
        gender: String,
        address: String,
        bp: String,
        temp: String,
        hb: String,
        maternal: String,
        vaccine: String,
        symptoms: String
    ) {
        viewModelScope.launch {
            val systolic = bp.split("/").firstOrNull()?.toIntOrNull() ?: 120
            val diastolic = bp.split("/").lastOrNull()?.toIntOrNull() ?: 80
            
            val entity = PatientEntity(
                name = name,
                age = age,
                gender = gender,
                address = address,
                systolicBP = systolic,
                diastolicBP = diastolic,
                temperature = temp,
                hemoglobin = hb,
                pregnancyStatus = maternal,
                vaccinationRemarks = vaccine,
                reportedSymptoms = symptoms,
                ashaName = _userName.value
            )
            patientDao.insertPatient(entity)
            forceSync()
            scheduleAutoSync()
        }
    }

    private fun scheduleAutoSync() {
        val constraints = androidx.work.Constraints.Builder()
            .setRequiredNetworkType(androidx.work.NetworkType.CONNECTED)
            .build()

        val syncRequest = androidx.work.OneTimeWorkRequestBuilder<SyncWorker>()
            .setConstraints(constraints)
            .build()

        androidx.work.WorkManager.getInstance(getApplication())
            .enqueueUniqueWork("auto_sync", androidx.work.ExistingWorkPolicy.REPLACE, syncRequest)
    }

    fun syncData() {
        // Trigger background sync
        scheduleAutoSync()
        // Also try an immediate force sync for the demonstration
        forceSync()
    }

    fun forceSync() {
        viewModelScope.launch {
            _isSyncing.value = true
            _loginError.value = null
            try {
                val unsyncedPatients = patientDao.getUnsyncedPatients()
                unsyncedPatients.forEach { localPatient ->
                    val request = PatientRequest(
                        name = localPatient.name,
                        age = localPatient.age,
                        gender = localPatient.gender,
                        address = localPatient.address,
                        systolicBP = localPatient.systolicBP,
                        diastolicBP = localPatient.diastolicBP,
                        temperature = localPatient.temperature,
                        hemoglobin = localPatient.hemoglobin,
                        pregnancyStatus = localPatient.pregnancyStatus,
                        sugarLevel = 0, // Explicitly pass default
                        tbFollowup = "No", // Explicitly pass default
                        vaccinationRemarks = localPatient.vaccinationRemarks,
                        reportedSymptoms = localPatient.reportedSymptoms
                    )
                    RetrofitClient.apiService.syncPatient(request)
                    patientDao.markAsSynced(localPatient.id)
                }
                
                // Also sync alerts
                val unsyncedAlerts = patientDao.getUnsyncedAlerts()
                unsyncedAlerts.forEach { localAlert ->
                    val alertRequest = AlertSyncRequest(
                        patientName = localAlert.patientName,
                        riskFactor = localAlert.riskFactor,
                        timestamp = localAlert.timestamp
                    )
                    RetrofitClient.apiService.syncAlert(alertRequest)
                    patientDao.markAlertAsSynced(localAlert.id)
                }

                _loginError.value = null
                fetchMonthlyReport() // Refresh doctor data if needed
            } catch (e: Exception) {
                if (e is retrofit2.HttpException) {
                    val errorBody = e.response()?.errorBody()?.string()
                    _loginError.value = "Sync Failed: $errorBody"
                } else {
                    _loginError.value = "Sync Failed: ${e.message}"
                }
                e.printStackTrace()
            } finally {
                _isSyncing.value = false
            }
        }
    }

    fun triggerVoiceAlert() {
        viewModelScope.launch {
            try {
                // Find the first patient in the local list to use their ID, or use a default
                val firstPatient = patientDao.getAllPatients().firstOrNull()
                val request = AlertRequest(
                    patientId = firstPatient?.id ?: 1,
                    riskFactor = "Critical: High BP",
                    transcript = "Emergency! Patient ${firstPatient?.name ?: "Unknown"} needs urgent care."
                )
                RetrofitClient.apiService.triggerAlert(request)
                // Also update local alerts so ASHA sees it too
                if (firstPatient != null) {
                    patientDao.insertAlert(AlertEntity(patientName = firstPatient.name, riskFactor = "Critical: High BP"))
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
