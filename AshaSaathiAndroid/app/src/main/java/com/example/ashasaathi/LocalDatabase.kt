package com.example.ashasaathi

import android.content.Context
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "patients_local")
data class PatientEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val age: Int,
    val gender: String,
    val address: String,
    val systolicBP: Int,
    val diastolicBP: Int,
    val temperature: String,
    val hemoglobin: String,
    val pregnancyStatus: String,
    val vaccinationRemarks: String,
    val reportedSymptoms: String,
    val ashaName: String = "Unknown",
    val isSynced: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "alerts_local")
data class AlertEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val patientName: String,
    val riskFactor: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isSynced: Boolean = false
)

@Dao
interface PatientDao {
    @Insert
    suspend fun insertPatient(patient: PatientEntity)

    @Query("SELECT * FROM patients_local WHERE isSynced = 0")
    suspend fun getUnsyncedPatients(): List<PatientEntity>

    @Query("UPDATE patients_local SET isSynced = 1 WHERE id = :patientId")
    suspend fun markAsSynced(patientId: Int)

    @Query("SELECT * FROM patients_local ORDER BY timestamp DESC")
    suspend fun getAllPatients(): List<PatientEntity>

    @Query("SELECT * FROM patients_local ORDER BY timestamp DESC")
    fun getAllPatientsFlow(): Flow<List<PatientEntity>>

    @Query("SELECT * FROM patients_local WHERE id = :id")
    suspend fun getPatientById(id: Int): PatientEntity?

    @Query("DELETE FROM patients_local")
    suspend fun deleteAll()

    @Query("DELETE FROM patients_local WHERE id = :id")
    suspend fun deletePatient(id: Int)

    @Query("DELETE FROM alerts_local WHERE patientName = :name")
    suspend fun deleteAlertsForPatient(name: String)

    @Insert
    suspend fun insertAlert(alert: AlertEntity)

    @Query("SELECT * FROM alerts_local ORDER BY timestamp DESC")
    fun getAllAlertsFlow(): kotlinx.coroutines.flow.Flow<List<AlertEntity>>

    @Query("SELECT * FROM alerts_local WHERE isSynced = 0")
    suspend fun getUnsyncedAlerts(): List<AlertEntity>

    @Query("UPDATE alerts_local SET isSynced = 1 WHERE id = :alertId")
    suspend fun markAlertAsSynced(alertId: Int)

    @Query("SELECT COUNT(*) FROM alerts_local")
    fun getAlertCountFlow(): kotlinx.coroutines.flow.Flow<Int>
}

@Database(entities = [PatientEntity::class, AlertEntity::class], version = 3)
abstract class AppDatabase : RoomDatabase() {
    abstract fun patientDao(): PatientDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "asha_saathi_local_db"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
