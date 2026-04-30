package com.example.ashasaathi;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001J\u0011\u0010\u0002\u001a\u00020\u0003H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0004J\u0011\u0010\u0005\u001a\u00020\u0006H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0004J\u0011\u0010\u0007\u001a\u00020\bH\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0004J\u001b\u0010\t\u001a\u00020\n2\b\b\u0001\u0010\u000b\u001a\u00020\fH\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\rJ\u001b\u0010\u000e\u001a\u00020\u00012\b\b\u0001\u0010\u000f\u001a\u00020\u0010H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0011J\u001b\u0010\u0012\u001a\u00020\u00012\b\b\u0001\u0010\u0013\u001a\u00020\u0014H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0015J\u001b\u0010\u0016\u001a\u00020\u00012\b\b\u0001\u0010\u000f\u001a\u00020\u0017H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0018\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\u0019"}, d2 = {"Lcom/example/ashasaathi/ApiService;", "", "getAlerts", "Lcom/example/ashasaathi/AlertListResponse;", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getMonthlyReport", "Lcom/example/ashasaathi/MonthlyReportResponse;", "getPatients", "Lcom/example/ashasaathi/PatientListResponse;", "login", "Lcom/example/ashasaathi/LoginResponse;", "request", "Lcom/example/ashasaathi/LoginRequest;", "(Lcom/example/ashasaathi/LoginRequest;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "syncAlert", "alert", "Lcom/example/ashasaathi/AlertSyncRequest;", "(Lcom/example/ashasaathi/AlertSyncRequest;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "syncPatient", "patient", "Lcom/example/ashasaathi/PatientRequest;", "(Lcom/example/ashasaathi/PatientRequest;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "triggerAlert", "Lcom/example/ashasaathi/AlertRequest;", "(Lcom/example/ashasaathi/AlertRequest;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public abstract interface ApiService {
    
    @retrofit2.http.POST(value = "/api/login")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object login(@retrofit2.http.Body
    @org.jetbrains.annotations.NotNull
    com.example.ashasaathi.LoginRequest request, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super com.example.ashasaathi.LoginResponse> $completion);
    
    @retrofit2.http.POST(value = "/api/patients")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object syncPatient(@retrofit2.http.Body
    @org.jetbrains.annotations.NotNull
    com.example.ashasaathi.PatientRequest patient, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<java.lang.Object> $completion);
    
    @retrofit2.http.GET(value = "/api/patients")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object getPatients(@org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super com.example.ashasaathi.PatientListResponse> $completion);
    
    @retrofit2.http.POST(value = "/api/alerts")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object triggerAlert(@retrofit2.http.Body
    @org.jetbrains.annotations.NotNull
    com.example.ashasaathi.AlertRequest alert, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<java.lang.Object> $completion);
    
    @retrofit2.http.POST(value = "/api/alerts/sync")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object syncAlert(@retrofit2.http.Body
    @org.jetbrains.annotations.NotNull
    com.example.ashasaathi.AlertSyncRequest alert, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<java.lang.Object> $completion);
    
    @retrofit2.http.GET(value = "/api/reports/monthly")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object getMonthlyReport(@org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super com.example.ashasaathi.MonthlyReportResponse> $completion);
    
    @retrofit2.http.GET(value = "/api/alerts")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object getAlerts(@org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super com.example.ashasaathi.AlertListResponse> $completion);
}