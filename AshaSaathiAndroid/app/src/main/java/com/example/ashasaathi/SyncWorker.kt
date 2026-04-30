package com.example.ashasaathi

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class SyncWorker(appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val database = AppDatabase.getDatabase(applicationContext)
        val patientDao = database.patientDao()
        val unsyncedPatients = patientDao.getUnsyncedPatients()

        if (unsyncedPatients.isEmpty()) {
            return Result.success()
        }

        return try {
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
                    vaccinationRemarks = localPatient.vaccinationRemarks,
                    reportedSymptoms = localPatient.reportedSymptoms
                )
                RetrofitClient.apiService.syncPatient(request)
                patientDao.markAsSynced(localPatient.id)
                Log.d("SyncWorker", "Successfully synced patient: ${localPatient.name}")
            }

            // Sync Alerts
            val unsyncedAlerts = patientDao.getUnsyncedAlerts()
            unsyncedAlerts.forEach { localAlert ->
                val alertRequest = AlertSyncRequest(
                    patientName = localAlert.patientName,
                    riskFactor = localAlert.riskFactor,
                    timestamp = localAlert.timestamp
                )
                RetrofitClient.apiService.syncAlert(alertRequest)
                patientDao.markAlertAsSynced(localAlert.id)
                Log.d("SyncWorker", "Successfully synced alert for: ${localAlert.patientName}")
            }

            Result.success()
        }
 catch (e: Exception) {
            Log.e("SyncWorker", "Error syncing data: ${e.message}")
            Result.retry()
        }
    }
}
