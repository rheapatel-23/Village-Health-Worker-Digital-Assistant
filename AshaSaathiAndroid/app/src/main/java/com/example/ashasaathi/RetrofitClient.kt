package com.example.ashasaathi

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

// Models matching our Node.js backend
data class LoginRequest(
    val email: String,
    val password: String,
    val role: String
)

data class LoginResponse(
    val message: String,
    val user: UserData?
)

data class UserData(
    val id: Int,
    val email: String,
    val role: String
)

data class PatientRequest(
    val name: String,
    val age: Int,
    val gender: String,
    val address: String = "Unknown",
    val systolicBP: Int,
    val diastolicBP: Int,
    val temperature: String,
    val hemoglobin: String,
    val pregnancyStatus: String,
    val sugarLevel: Int = 0,
    val tbFollowup: String = "No",
    val vaccinationRemarks: String,
    val reportedSymptoms: String
)

data class AlertSyncRequest(
    val patientName: String,
    val riskFactor: String,
    val timestamp: Long
)

data class AlertRequest(
    val patientId: Int,
    val riskFactor: String,
    val transcript: String
)

data class PatientListResponse(
    val data: List<PatientData>
)

data class PatientData(
    val id: Int,
    val name: String,
    val age: Int,
    val gender: String,
    val systolicBP: Int,
    val status: String,
    val timestamp: String
)

// Retrofit API Interface
interface ApiService {
    @POST("/api/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @POST("/api/patients")
    suspend fun syncPatient(@Body patient: PatientRequest): Any

    @GET("/api/patients")
    suspend fun getPatients(): PatientListResponse

    @POST("/api/alerts")
    suspend fun triggerAlert(@Body alert: AlertRequest): Any

    @POST("/api/alerts/sync")
    suspend fun syncAlert(@Body alert: AlertSyncRequest): Any

    @GET("/api/reports/monthly")
    suspend fun getMonthlyReport(): MonthlyReportResponse

    @GET("/api/alerts")
    suspend fun getAlerts(): AlertListResponse
}

data class AlertListResponse(
    val data: List<AlertData>
)

data class AlertData(
    val id: Int,
    val patientId: Int,
    val patientName: String,
    val riskFactor: String,
    val transcript: String,
    val timestamp: String
)

data class MonthlyReportResponse(
    val month: String,
    val summary: ReportSummary
)

data class ReportSummary(
    val totalPatients: Int,
    val totalAlerts: Int,
    val highBPCases: Int
)

// Retrofit Client Singleton
object RetrofitClient {
    // IMPORTANT: 
    // - Preferred for emulator after running adb reverse:
    //   "http://127.0.0.1:3001/"
    // - Alternate emulator host mapping:
    //   "http://10.0.2.2:3001/"
    // - If you are testing on a real physical phone, you MUST change this to your computer's Wi-Fi IPv4 address (e.g., "http://192.168.1.5:3001/").
    const val BASE_URL = "http://127.0.0.1:3001/"

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
