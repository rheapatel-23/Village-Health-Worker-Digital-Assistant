package com.example.ashasaathi;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u000f\bg\u0018\u00002\u00020\u0001J\u0019\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0006J\u0011\u0010\u0007\u001a\u00020\u0003H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\bJ\u0019\u0010\t\u001a\u00020\u00032\u0006\u0010\n\u001a\u00020\u000bH\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\fJ\u000e\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000b0\u000eH\'J\u0014\u0010\u000f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00110\u00100\u000eH\'J\u0017\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00130\u0010H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\bJ\u0014\u0010\u0014\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00130\u00100\u000eH\'J\u001b\u0010\u0015\u001a\u0004\u0018\u00010\u00132\u0006\u0010\n\u001a\u00020\u000bH\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\fJ\u0017\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00110\u0010H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\bJ\u0017\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00130\u0010H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\bJ\u0019\u0010\u0018\u001a\u00020\u00032\u0006\u0010\u0019\u001a\u00020\u0011H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u001aJ\u0019\u0010\u001b\u001a\u00020\u00032\u0006\u0010\u001c\u001a\u00020\u0013H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u001dJ\u0019\u0010\u001e\u001a\u00020\u00032\u0006\u0010\u001f\u001a\u00020\u000bH\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\fJ\u0019\u0010 \u001a\u00020\u00032\u0006\u0010!\u001a\u00020\u000bH\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\f\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\""}, d2 = {"Lcom/example/ashasaathi/PatientDao;", "", "deleteAlertsForPatient", "", "name", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteAll", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deletePatient", "id", "", "(ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAlertCountFlow", "Lkotlinx/coroutines/flow/Flow;", "getAllAlertsFlow", "", "Lcom/example/ashasaathi/AlertEntity;", "getAllPatients", "Lcom/example/ashasaathi/PatientEntity;", "getAllPatientsFlow", "getPatientById", "getUnsyncedAlerts", "getUnsyncedPatients", "insertAlert", "alert", "(Lcom/example/ashasaathi/AlertEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insertPatient", "patient", "(Lcom/example/ashasaathi/PatientEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "markAlertAsSynced", "alertId", "markAsSynced", "patientId", "app_debug"})
@androidx.room.Dao
public abstract interface PatientDao {
    
    @androidx.room.Insert
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object insertPatient(@org.jetbrains.annotations.NotNull
    com.example.ashasaathi.PatientEntity patient, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM patients_local WHERE isSynced = 0")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object getUnsyncedPatients(@org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super java.util.List<com.example.ashasaathi.PatientEntity>> $completion);
    
    @androidx.room.Query(value = "UPDATE patients_local SET isSynced = 1 WHERE id = :patientId")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object markAsSynced(int patientId, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM patients_local ORDER BY timestamp DESC")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object getAllPatients(@org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super java.util.List<com.example.ashasaathi.PatientEntity>> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM patients_local ORDER BY timestamp DESC")
    @org.jetbrains.annotations.NotNull
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.example.ashasaathi.PatientEntity>> getAllPatientsFlow();
    
    @androidx.room.Query(value = "SELECT * FROM patients_local WHERE id = :id")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object getPatientById(int id, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super com.example.ashasaathi.PatientEntity> $completion);
    
    @androidx.room.Query(value = "DELETE FROM patients_local")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object deleteAll(@org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM patients_local WHERE id = :id")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object deletePatient(int id, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM alerts_local WHERE patientName = :name")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object deleteAlertsForPatient(@org.jetbrains.annotations.NotNull
    java.lang.String name, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Insert
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object insertAlert(@org.jetbrains.annotations.NotNull
    com.example.ashasaathi.AlertEntity alert, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM alerts_local ORDER BY timestamp DESC")
    @org.jetbrains.annotations.NotNull
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.example.ashasaathi.AlertEntity>> getAllAlertsFlow();
    
    @androidx.room.Query(value = "SELECT * FROM alerts_local WHERE isSynced = 0")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object getUnsyncedAlerts(@org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super java.util.List<com.example.ashasaathi.AlertEntity>> $completion);
    
    @androidx.room.Query(value = "UPDATE alerts_local SET isSynced = 1 WHERE id = :alertId")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object markAlertAsSynced(int alertId, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "SELECT COUNT(*) FROM alerts_local")
    @org.jetbrains.annotations.NotNull
    public abstract kotlinx.coroutines.flow.Flow<java.lang.Integer> getAlertCountFlow();
}