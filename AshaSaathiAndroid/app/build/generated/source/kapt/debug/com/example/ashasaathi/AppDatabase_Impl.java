package com.example.ashasaathi;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile PatientDao _patientDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(3) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `patients_local` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `age` INTEGER NOT NULL, `gender` TEXT NOT NULL, `address` TEXT NOT NULL, `systolicBP` INTEGER NOT NULL, `diastolicBP` INTEGER NOT NULL, `temperature` TEXT NOT NULL, `hemoglobin` TEXT NOT NULL, `pregnancyStatus` TEXT NOT NULL, `vaccinationRemarks` TEXT NOT NULL, `reportedSymptoms` TEXT NOT NULL, `ashaName` TEXT NOT NULL, `isSynced` INTEGER NOT NULL, `timestamp` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `alerts_local` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `patientName` TEXT NOT NULL, `riskFactor` TEXT NOT NULL, `timestamp` INTEGER NOT NULL, `isSynced` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '7a2ec924ab860ececb8ad32e32c22c76')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `patients_local`");
        db.execSQL("DROP TABLE IF EXISTS `alerts_local`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsPatientsLocal = new HashMap<String, TableInfo.Column>(15);
        _columnsPatientsLocal.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPatientsLocal.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPatientsLocal.put("age", new TableInfo.Column("age", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPatientsLocal.put("gender", new TableInfo.Column("gender", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPatientsLocal.put("address", new TableInfo.Column("address", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPatientsLocal.put("systolicBP", new TableInfo.Column("systolicBP", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPatientsLocal.put("diastolicBP", new TableInfo.Column("diastolicBP", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPatientsLocal.put("temperature", new TableInfo.Column("temperature", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPatientsLocal.put("hemoglobin", new TableInfo.Column("hemoglobin", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPatientsLocal.put("pregnancyStatus", new TableInfo.Column("pregnancyStatus", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPatientsLocal.put("vaccinationRemarks", new TableInfo.Column("vaccinationRemarks", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPatientsLocal.put("reportedSymptoms", new TableInfo.Column("reportedSymptoms", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPatientsLocal.put("ashaName", new TableInfo.Column("ashaName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPatientsLocal.put("isSynced", new TableInfo.Column("isSynced", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPatientsLocal.put("timestamp", new TableInfo.Column("timestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysPatientsLocal = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesPatientsLocal = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoPatientsLocal = new TableInfo("patients_local", _columnsPatientsLocal, _foreignKeysPatientsLocal, _indicesPatientsLocal);
        final TableInfo _existingPatientsLocal = TableInfo.read(db, "patients_local");
        if (!_infoPatientsLocal.equals(_existingPatientsLocal)) {
          return new RoomOpenHelper.ValidationResult(false, "patients_local(com.example.ashasaathi.PatientEntity).\n"
                  + " Expected:\n" + _infoPatientsLocal + "\n"
                  + " Found:\n" + _existingPatientsLocal);
        }
        final HashMap<String, TableInfo.Column> _columnsAlertsLocal = new HashMap<String, TableInfo.Column>(5);
        _columnsAlertsLocal.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAlertsLocal.put("patientName", new TableInfo.Column("patientName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAlertsLocal.put("riskFactor", new TableInfo.Column("riskFactor", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAlertsLocal.put("timestamp", new TableInfo.Column("timestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAlertsLocal.put("isSynced", new TableInfo.Column("isSynced", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysAlertsLocal = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesAlertsLocal = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoAlertsLocal = new TableInfo("alerts_local", _columnsAlertsLocal, _foreignKeysAlertsLocal, _indicesAlertsLocal);
        final TableInfo _existingAlertsLocal = TableInfo.read(db, "alerts_local");
        if (!_infoAlertsLocal.equals(_existingAlertsLocal)) {
          return new RoomOpenHelper.ValidationResult(false, "alerts_local(com.example.ashasaathi.AlertEntity).\n"
                  + " Expected:\n" + _infoAlertsLocal + "\n"
                  + " Found:\n" + _existingAlertsLocal);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "7a2ec924ab860ececb8ad32e32c22c76", "7acd3f64d24a414b91a260daee117d35");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "patients_local","alerts_local");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `patients_local`");
      _db.execSQL("DELETE FROM `alerts_local`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(PatientDao.class, PatientDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public PatientDao patientDao() {
    if (_patientDao != null) {
      return _patientDao;
    } else {
      synchronized(this) {
        if(_patientDao == null) {
          _patientDao = new PatientDao_Impl(this);
        }
        return _patientDao;
      }
    }
  }
}
