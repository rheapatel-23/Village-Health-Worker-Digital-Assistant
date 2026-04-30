package com.example.ashasaathi;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class PatientDao_Impl implements PatientDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<PatientEntity> __insertionAdapterOfPatientEntity;

  private final EntityInsertionAdapter<AlertEntity> __insertionAdapterOfAlertEntity;

  private final SharedSQLiteStatement __preparedStmtOfMarkAsSynced;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  private final SharedSQLiteStatement __preparedStmtOfDeletePatient;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAlertsForPatient;

  private final SharedSQLiteStatement __preparedStmtOfMarkAlertAsSynced;

  public PatientDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfPatientEntity = new EntityInsertionAdapter<PatientEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `patients_local` (`id`,`name`,`age`,`gender`,`address`,`systolicBP`,`diastolicBP`,`temperature`,`hemoglobin`,`pregnancyStatus`,`vaccinationRemarks`,`reportedSymptoms`,`ashaName`,`isSynced`,`timestamp`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final PatientEntity entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getName() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getName());
        }
        statement.bindLong(3, entity.getAge());
        if (entity.getGender() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getGender());
        }
        if (entity.getAddress() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getAddress());
        }
        statement.bindLong(6, entity.getSystolicBP());
        statement.bindLong(7, entity.getDiastolicBP());
        if (entity.getTemperature() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getTemperature());
        }
        if (entity.getHemoglobin() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getHemoglobin());
        }
        if (entity.getPregnancyStatus() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getPregnancyStatus());
        }
        if (entity.getVaccinationRemarks() == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, entity.getVaccinationRemarks());
        }
        if (entity.getReportedSymptoms() == null) {
          statement.bindNull(12);
        } else {
          statement.bindString(12, entity.getReportedSymptoms());
        }
        if (entity.getAshaName() == null) {
          statement.bindNull(13);
        } else {
          statement.bindString(13, entity.getAshaName());
        }
        final int _tmp = entity.isSynced() ? 1 : 0;
        statement.bindLong(14, _tmp);
        statement.bindLong(15, entity.getTimestamp());
      }
    };
    this.__insertionAdapterOfAlertEntity = new EntityInsertionAdapter<AlertEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `alerts_local` (`id`,`patientName`,`riskFactor`,`timestamp`,`isSynced`) VALUES (nullif(?, 0),?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final AlertEntity entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getPatientName() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getPatientName());
        }
        if (entity.getRiskFactor() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getRiskFactor());
        }
        statement.bindLong(4, entity.getTimestamp());
        final int _tmp = entity.isSynced() ? 1 : 0;
        statement.bindLong(5, _tmp);
      }
    };
    this.__preparedStmtOfMarkAsSynced = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE patients_local SET isSynced = 1 WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM patients_local";
        return _query;
      }
    };
    this.__preparedStmtOfDeletePatient = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM patients_local WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteAlertsForPatient = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM alerts_local WHERE patientName = ?";
        return _query;
      }
    };
    this.__preparedStmtOfMarkAlertAsSynced = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE alerts_local SET isSynced = 1 WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertPatient(final PatientEntity patient,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfPatientEntity.insert(patient);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertAlert(final AlertEntity alert, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfAlertEntity.insert(alert);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object markAsSynced(final int patientId, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfMarkAsSynced.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, patientId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfMarkAsSynced.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteAll(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAll.acquire();
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteAll.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deletePatient(final int id, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeletePatient.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, id);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeletePatient.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteAlertsForPatient(final String name,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAlertsForPatient.acquire();
        int _argIndex = 1;
        if (name == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, name);
        }
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteAlertsForPatient.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object markAlertAsSynced(final int alertId, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfMarkAlertAsSynced.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, alertId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfMarkAlertAsSynced.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object getUnsyncedPatients(final Continuation<? super List<PatientEntity>> $completion) {
    final String _sql = "SELECT * FROM patients_local WHERE isSynced = 0";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<PatientEntity>>() {
      @Override
      @NonNull
      public List<PatientEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfAge = CursorUtil.getColumnIndexOrThrow(_cursor, "age");
          final int _cursorIndexOfGender = CursorUtil.getColumnIndexOrThrow(_cursor, "gender");
          final int _cursorIndexOfAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "address");
          final int _cursorIndexOfSystolicBP = CursorUtil.getColumnIndexOrThrow(_cursor, "systolicBP");
          final int _cursorIndexOfDiastolicBP = CursorUtil.getColumnIndexOrThrow(_cursor, "diastolicBP");
          final int _cursorIndexOfTemperature = CursorUtil.getColumnIndexOrThrow(_cursor, "temperature");
          final int _cursorIndexOfHemoglobin = CursorUtil.getColumnIndexOrThrow(_cursor, "hemoglobin");
          final int _cursorIndexOfPregnancyStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "pregnancyStatus");
          final int _cursorIndexOfVaccinationRemarks = CursorUtil.getColumnIndexOrThrow(_cursor, "vaccinationRemarks");
          final int _cursorIndexOfReportedSymptoms = CursorUtil.getColumnIndexOrThrow(_cursor, "reportedSymptoms");
          final int _cursorIndexOfAshaName = CursorUtil.getColumnIndexOrThrow(_cursor, "ashaName");
          final int _cursorIndexOfIsSynced = CursorUtil.getColumnIndexOrThrow(_cursor, "isSynced");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final List<PatientEntity> _result = new ArrayList<PatientEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final PatientEntity _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpName;
            if (_cursor.isNull(_cursorIndexOfName)) {
              _tmpName = null;
            } else {
              _tmpName = _cursor.getString(_cursorIndexOfName);
            }
            final int _tmpAge;
            _tmpAge = _cursor.getInt(_cursorIndexOfAge);
            final String _tmpGender;
            if (_cursor.isNull(_cursorIndexOfGender)) {
              _tmpGender = null;
            } else {
              _tmpGender = _cursor.getString(_cursorIndexOfGender);
            }
            final String _tmpAddress;
            if (_cursor.isNull(_cursorIndexOfAddress)) {
              _tmpAddress = null;
            } else {
              _tmpAddress = _cursor.getString(_cursorIndexOfAddress);
            }
            final int _tmpSystolicBP;
            _tmpSystolicBP = _cursor.getInt(_cursorIndexOfSystolicBP);
            final int _tmpDiastolicBP;
            _tmpDiastolicBP = _cursor.getInt(_cursorIndexOfDiastolicBP);
            final String _tmpTemperature;
            if (_cursor.isNull(_cursorIndexOfTemperature)) {
              _tmpTemperature = null;
            } else {
              _tmpTemperature = _cursor.getString(_cursorIndexOfTemperature);
            }
            final String _tmpHemoglobin;
            if (_cursor.isNull(_cursorIndexOfHemoglobin)) {
              _tmpHemoglobin = null;
            } else {
              _tmpHemoglobin = _cursor.getString(_cursorIndexOfHemoglobin);
            }
            final String _tmpPregnancyStatus;
            if (_cursor.isNull(_cursorIndexOfPregnancyStatus)) {
              _tmpPregnancyStatus = null;
            } else {
              _tmpPregnancyStatus = _cursor.getString(_cursorIndexOfPregnancyStatus);
            }
            final String _tmpVaccinationRemarks;
            if (_cursor.isNull(_cursorIndexOfVaccinationRemarks)) {
              _tmpVaccinationRemarks = null;
            } else {
              _tmpVaccinationRemarks = _cursor.getString(_cursorIndexOfVaccinationRemarks);
            }
            final String _tmpReportedSymptoms;
            if (_cursor.isNull(_cursorIndexOfReportedSymptoms)) {
              _tmpReportedSymptoms = null;
            } else {
              _tmpReportedSymptoms = _cursor.getString(_cursorIndexOfReportedSymptoms);
            }
            final String _tmpAshaName;
            if (_cursor.isNull(_cursorIndexOfAshaName)) {
              _tmpAshaName = null;
            } else {
              _tmpAshaName = _cursor.getString(_cursorIndexOfAshaName);
            }
            final boolean _tmpIsSynced;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsSynced);
            _tmpIsSynced = _tmp != 0;
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            _item = new PatientEntity(_tmpId,_tmpName,_tmpAge,_tmpGender,_tmpAddress,_tmpSystolicBP,_tmpDiastolicBP,_tmpTemperature,_tmpHemoglobin,_tmpPregnancyStatus,_tmpVaccinationRemarks,_tmpReportedSymptoms,_tmpAshaName,_tmpIsSynced,_tmpTimestamp);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getAllPatients(final Continuation<? super List<PatientEntity>> $completion) {
    final String _sql = "SELECT * FROM patients_local ORDER BY timestamp DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<PatientEntity>>() {
      @Override
      @NonNull
      public List<PatientEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfAge = CursorUtil.getColumnIndexOrThrow(_cursor, "age");
          final int _cursorIndexOfGender = CursorUtil.getColumnIndexOrThrow(_cursor, "gender");
          final int _cursorIndexOfAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "address");
          final int _cursorIndexOfSystolicBP = CursorUtil.getColumnIndexOrThrow(_cursor, "systolicBP");
          final int _cursorIndexOfDiastolicBP = CursorUtil.getColumnIndexOrThrow(_cursor, "diastolicBP");
          final int _cursorIndexOfTemperature = CursorUtil.getColumnIndexOrThrow(_cursor, "temperature");
          final int _cursorIndexOfHemoglobin = CursorUtil.getColumnIndexOrThrow(_cursor, "hemoglobin");
          final int _cursorIndexOfPregnancyStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "pregnancyStatus");
          final int _cursorIndexOfVaccinationRemarks = CursorUtil.getColumnIndexOrThrow(_cursor, "vaccinationRemarks");
          final int _cursorIndexOfReportedSymptoms = CursorUtil.getColumnIndexOrThrow(_cursor, "reportedSymptoms");
          final int _cursorIndexOfAshaName = CursorUtil.getColumnIndexOrThrow(_cursor, "ashaName");
          final int _cursorIndexOfIsSynced = CursorUtil.getColumnIndexOrThrow(_cursor, "isSynced");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final List<PatientEntity> _result = new ArrayList<PatientEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final PatientEntity _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpName;
            if (_cursor.isNull(_cursorIndexOfName)) {
              _tmpName = null;
            } else {
              _tmpName = _cursor.getString(_cursorIndexOfName);
            }
            final int _tmpAge;
            _tmpAge = _cursor.getInt(_cursorIndexOfAge);
            final String _tmpGender;
            if (_cursor.isNull(_cursorIndexOfGender)) {
              _tmpGender = null;
            } else {
              _tmpGender = _cursor.getString(_cursorIndexOfGender);
            }
            final String _tmpAddress;
            if (_cursor.isNull(_cursorIndexOfAddress)) {
              _tmpAddress = null;
            } else {
              _tmpAddress = _cursor.getString(_cursorIndexOfAddress);
            }
            final int _tmpSystolicBP;
            _tmpSystolicBP = _cursor.getInt(_cursorIndexOfSystolicBP);
            final int _tmpDiastolicBP;
            _tmpDiastolicBP = _cursor.getInt(_cursorIndexOfDiastolicBP);
            final String _tmpTemperature;
            if (_cursor.isNull(_cursorIndexOfTemperature)) {
              _tmpTemperature = null;
            } else {
              _tmpTemperature = _cursor.getString(_cursorIndexOfTemperature);
            }
            final String _tmpHemoglobin;
            if (_cursor.isNull(_cursorIndexOfHemoglobin)) {
              _tmpHemoglobin = null;
            } else {
              _tmpHemoglobin = _cursor.getString(_cursorIndexOfHemoglobin);
            }
            final String _tmpPregnancyStatus;
            if (_cursor.isNull(_cursorIndexOfPregnancyStatus)) {
              _tmpPregnancyStatus = null;
            } else {
              _tmpPregnancyStatus = _cursor.getString(_cursorIndexOfPregnancyStatus);
            }
            final String _tmpVaccinationRemarks;
            if (_cursor.isNull(_cursorIndexOfVaccinationRemarks)) {
              _tmpVaccinationRemarks = null;
            } else {
              _tmpVaccinationRemarks = _cursor.getString(_cursorIndexOfVaccinationRemarks);
            }
            final String _tmpReportedSymptoms;
            if (_cursor.isNull(_cursorIndexOfReportedSymptoms)) {
              _tmpReportedSymptoms = null;
            } else {
              _tmpReportedSymptoms = _cursor.getString(_cursorIndexOfReportedSymptoms);
            }
            final String _tmpAshaName;
            if (_cursor.isNull(_cursorIndexOfAshaName)) {
              _tmpAshaName = null;
            } else {
              _tmpAshaName = _cursor.getString(_cursorIndexOfAshaName);
            }
            final boolean _tmpIsSynced;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsSynced);
            _tmpIsSynced = _tmp != 0;
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            _item = new PatientEntity(_tmpId,_tmpName,_tmpAge,_tmpGender,_tmpAddress,_tmpSystolicBP,_tmpDiastolicBP,_tmpTemperature,_tmpHemoglobin,_tmpPregnancyStatus,_tmpVaccinationRemarks,_tmpReportedSymptoms,_tmpAshaName,_tmpIsSynced,_tmpTimestamp);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<PatientEntity>> getAllPatientsFlow() {
    final String _sql = "SELECT * FROM patients_local ORDER BY timestamp DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"patients_local"}, new Callable<List<PatientEntity>>() {
      @Override
      @NonNull
      public List<PatientEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfAge = CursorUtil.getColumnIndexOrThrow(_cursor, "age");
          final int _cursorIndexOfGender = CursorUtil.getColumnIndexOrThrow(_cursor, "gender");
          final int _cursorIndexOfAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "address");
          final int _cursorIndexOfSystolicBP = CursorUtil.getColumnIndexOrThrow(_cursor, "systolicBP");
          final int _cursorIndexOfDiastolicBP = CursorUtil.getColumnIndexOrThrow(_cursor, "diastolicBP");
          final int _cursorIndexOfTemperature = CursorUtil.getColumnIndexOrThrow(_cursor, "temperature");
          final int _cursorIndexOfHemoglobin = CursorUtil.getColumnIndexOrThrow(_cursor, "hemoglobin");
          final int _cursorIndexOfPregnancyStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "pregnancyStatus");
          final int _cursorIndexOfVaccinationRemarks = CursorUtil.getColumnIndexOrThrow(_cursor, "vaccinationRemarks");
          final int _cursorIndexOfReportedSymptoms = CursorUtil.getColumnIndexOrThrow(_cursor, "reportedSymptoms");
          final int _cursorIndexOfAshaName = CursorUtil.getColumnIndexOrThrow(_cursor, "ashaName");
          final int _cursorIndexOfIsSynced = CursorUtil.getColumnIndexOrThrow(_cursor, "isSynced");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final List<PatientEntity> _result = new ArrayList<PatientEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final PatientEntity _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpName;
            if (_cursor.isNull(_cursorIndexOfName)) {
              _tmpName = null;
            } else {
              _tmpName = _cursor.getString(_cursorIndexOfName);
            }
            final int _tmpAge;
            _tmpAge = _cursor.getInt(_cursorIndexOfAge);
            final String _tmpGender;
            if (_cursor.isNull(_cursorIndexOfGender)) {
              _tmpGender = null;
            } else {
              _tmpGender = _cursor.getString(_cursorIndexOfGender);
            }
            final String _tmpAddress;
            if (_cursor.isNull(_cursorIndexOfAddress)) {
              _tmpAddress = null;
            } else {
              _tmpAddress = _cursor.getString(_cursorIndexOfAddress);
            }
            final int _tmpSystolicBP;
            _tmpSystolicBP = _cursor.getInt(_cursorIndexOfSystolicBP);
            final int _tmpDiastolicBP;
            _tmpDiastolicBP = _cursor.getInt(_cursorIndexOfDiastolicBP);
            final String _tmpTemperature;
            if (_cursor.isNull(_cursorIndexOfTemperature)) {
              _tmpTemperature = null;
            } else {
              _tmpTemperature = _cursor.getString(_cursorIndexOfTemperature);
            }
            final String _tmpHemoglobin;
            if (_cursor.isNull(_cursorIndexOfHemoglobin)) {
              _tmpHemoglobin = null;
            } else {
              _tmpHemoglobin = _cursor.getString(_cursorIndexOfHemoglobin);
            }
            final String _tmpPregnancyStatus;
            if (_cursor.isNull(_cursorIndexOfPregnancyStatus)) {
              _tmpPregnancyStatus = null;
            } else {
              _tmpPregnancyStatus = _cursor.getString(_cursorIndexOfPregnancyStatus);
            }
            final String _tmpVaccinationRemarks;
            if (_cursor.isNull(_cursorIndexOfVaccinationRemarks)) {
              _tmpVaccinationRemarks = null;
            } else {
              _tmpVaccinationRemarks = _cursor.getString(_cursorIndexOfVaccinationRemarks);
            }
            final String _tmpReportedSymptoms;
            if (_cursor.isNull(_cursorIndexOfReportedSymptoms)) {
              _tmpReportedSymptoms = null;
            } else {
              _tmpReportedSymptoms = _cursor.getString(_cursorIndexOfReportedSymptoms);
            }
            final String _tmpAshaName;
            if (_cursor.isNull(_cursorIndexOfAshaName)) {
              _tmpAshaName = null;
            } else {
              _tmpAshaName = _cursor.getString(_cursorIndexOfAshaName);
            }
            final boolean _tmpIsSynced;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsSynced);
            _tmpIsSynced = _tmp != 0;
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            _item = new PatientEntity(_tmpId,_tmpName,_tmpAge,_tmpGender,_tmpAddress,_tmpSystolicBP,_tmpDiastolicBP,_tmpTemperature,_tmpHemoglobin,_tmpPregnancyStatus,_tmpVaccinationRemarks,_tmpReportedSymptoms,_tmpAshaName,_tmpIsSynced,_tmpTimestamp);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getPatientById(final int id,
      final Continuation<? super PatientEntity> $completion) {
    final String _sql = "SELECT * FROM patients_local WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<PatientEntity>() {
      @Override
      @Nullable
      public PatientEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfAge = CursorUtil.getColumnIndexOrThrow(_cursor, "age");
          final int _cursorIndexOfGender = CursorUtil.getColumnIndexOrThrow(_cursor, "gender");
          final int _cursorIndexOfAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "address");
          final int _cursorIndexOfSystolicBP = CursorUtil.getColumnIndexOrThrow(_cursor, "systolicBP");
          final int _cursorIndexOfDiastolicBP = CursorUtil.getColumnIndexOrThrow(_cursor, "diastolicBP");
          final int _cursorIndexOfTemperature = CursorUtil.getColumnIndexOrThrow(_cursor, "temperature");
          final int _cursorIndexOfHemoglobin = CursorUtil.getColumnIndexOrThrow(_cursor, "hemoglobin");
          final int _cursorIndexOfPregnancyStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "pregnancyStatus");
          final int _cursorIndexOfVaccinationRemarks = CursorUtil.getColumnIndexOrThrow(_cursor, "vaccinationRemarks");
          final int _cursorIndexOfReportedSymptoms = CursorUtil.getColumnIndexOrThrow(_cursor, "reportedSymptoms");
          final int _cursorIndexOfAshaName = CursorUtil.getColumnIndexOrThrow(_cursor, "ashaName");
          final int _cursorIndexOfIsSynced = CursorUtil.getColumnIndexOrThrow(_cursor, "isSynced");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final PatientEntity _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpName;
            if (_cursor.isNull(_cursorIndexOfName)) {
              _tmpName = null;
            } else {
              _tmpName = _cursor.getString(_cursorIndexOfName);
            }
            final int _tmpAge;
            _tmpAge = _cursor.getInt(_cursorIndexOfAge);
            final String _tmpGender;
            if (_cursor.isNull(_cursorIndexOfGender)) {
              _tmpGender = null;
            } else {
              _tmpGender = _cursor.getString(_cursorIndexOfGender);
            }
            final String _tmpAddress;
            if (_cursor.isNull(_cursorIndexOfAddress)) {
              _tmpAddress = null;
            } else {
              _tmpAddress = _cursor.getString(_cursorIndexOfAddress);
            }
            final int _tmpSystolicBP;
            _tmpSystolicBP = _cursor.getInt(_cursorIndexOfSystolicBP);
            final int _tmpDiastolicBP;
            _tmpDiastolicBP = _cursor.getInt(_cursorIndexOfDiastolicBP);
            final String _tmpTemperature;
            if (_cursor.isNull(_cursorIndexOfTemperature)) {
              _tmpTemperature = null;
            } else {
              _tmpTemperature = _cursor.getString(_cursorIndexOfTemperature);
            }
            final String _tmpHemoglobin;
            if (_cursor.isNull(_cursorIndexOfHemoglobin)) {
              _tmpHemoglobin = null;
            } else {
              _tmpHemoglobin = _cursor.getString(_cursorIndexOfHemoglobin);
            }
            final String _tmpPregnancyStatus;
            if (_cursor.isNull(_cursorIndexOfPregnancyStatus)) {
              _tmpPregnancyStatus = null;
            } else {
              _tmpPregnancyStatus = _cursor.getString(_cursorIndexOfPregnancyStatus);
            }
            final String _tmpVaccinationRemarks;
            if (_cursor.isNull(_cursorIndexOfVaccinationRemarks)) {
              _tmpVaccinationRemarks = null;
            } else {
              _tmpVaccinationRemarks = _cursor.getString(_cursorIndexOfVaccinationRemarks);
            }
            final String _tmpReportedSymptoms;
            if (_cursor.isNull(_cursorIndexOfReportedSymptoms)) {
              _tmpReportedSymptoms = null;
            } else {
              _tmpReportedSymptoms = _cursor.getString(_cursorIndexOfReportedSymptoms);
            }
            final String _tmpAshaName;
            if (_cursor.isNull(_cursorIndexOfAshaName)) {
              _tmpAshaName = null;
            } else {
              _tmpAshaName = _cursor.getString(_cursorIndexOfAshaName);
            }
            final boolean _tmpIsSynced;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsSynced);
            _tmpIsSynced = _tmp != 0;
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            _result = new PatientEntity(_tmpId,_tmpName,_tmpAge,_tmpGender,_tmpAddress,_tmpSystolicBP,_tmpDiastolicBP,_tmpTemperature,_tmpHemoglobin,_tmpPregnancyStatus,_tmpVaccinationRemarks,_tmpReportedSymptoms,_tmpAshaName,_tmpIsSynced,_tmpTimestamp);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<AlertEntity>> getAllAlertsFlow() {
    final String _sql = "SELECT * FROM alerts_local ORDER BY timestamp DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"alerts_local"}, new Callable<List<AlertEntity>>() {
      @Override
      @NonNull
      public List<AlertEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPatientName = CursorUtil.getColumnIndexOrThrow(_cursor, "patientName");
          final int _cursorIndexOfRiskFactor = CursorUtil.getColumnIndexOrThrow(_cursor, "riskFactor");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfIsSynced = CursorUtil.getColumnIndexOrThrow(_cursor, "isSynced");
          final List<AlertEntity> _result = new ArrayList<AlertEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final AlertEntity _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpPatientName;
            if (_cursor.isNull(_cursorIndexOfPatientName)) {
              _tmpPatientName = null;
            } else {
              _tmpPatientName = _cursor.getString(_cursorIndexOfPatientName);
            }
            final String _tmpRiskFactor;
            if (_cursor.isNull(_cursorIndexOfRiskFactor)) {
              _tmpRiskFactor = null;
            } else {
              _tmpRiskFactor = _cursor.getString(_cursorIndexOfRiskFactor);
            }
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final boolean _tmpIsSynced;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsSynced);
            _tmpIsSynced = _tmp != 0;
            _item = new AlertEntity(_tmpId,_tmpPatientName,_tmpRiskFactor,_tmpTimestamp,_tmpIsSynced);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getUnsyncedAlerts(final Continuation<? super List<AlertEntity>> $completion) {
    final String _sql = "SELECT * FROM alerts_local WHERE isSynced = 0";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<AlertEntity>>() {
      @Override
      @NonNull
      public List<AlertEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPatientName = CursorUtil.getColumnIndexOrThrow(_cursor, "patientName");
          final int _cursorIndexOfRiskFactor = CursorUtil.getColumnIndexOrThrow(_cursor, "riskFactor");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfIsSynced = CursorUtil.getColumnIndexOrThrow(_cursor, "isSynced");
          final List<AlertEntity> _result = new ArrayList<AlertEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final AlertEntity _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpPatientName;
            if (_cursor.isNull(_cursorIndexOfPatientName)) {
              _tmpPatientName = null;
            } else {
              _tmpPatientName = _cursor.getString(_cursorIndexOfPatientName);
            }
            final String _tmpRiskFactor;
            if (_cursor.isNull(_cursorIndexOfRiskFactor)) {
              _tmpRiskFactor = null;
            } else {
              _tmpRiskFactor = _cursor.getString(_cursorIndexOfRiskFactor);
            }
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final boolean _tmpIsSynced;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsSynced);
            _tmpIsSynced = _tmp != 0;
            _item = new AlertEntity(_tmpId,_tmpPatientName,_tmpRiskFactor,_tmpTimestamp,_tmpIsSynced);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<Integer> getAlertCountFlow() {
    final String _sql = "SELECT COUNT(*) FROM alerts_local";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"alerts_local"}, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final Integer _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getInt(0);
            }
            _result = _tmp;
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
