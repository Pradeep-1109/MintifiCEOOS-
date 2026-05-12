package com.mintifi.ceoos.data.database;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.mintifi.ceoos.data.model.OfflineQueueItem;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Long;
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
public final class OfflineQueueDao_Impl implements OfflineQueueDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<OfflineQueueItem> __insertionAdapterOfOfflineQueueItem;

  private final EntityDeletionOrUpdateAdapter<OfflineQueueItem> __deletionAdapterOfOfflineQueueItem;

  private final SharedSQLiteStatement __preparedStmtOfMarkProcessing;

  private final SharedSQLiteStatement __preparedStmtOfMarkDone;

  private final SharedSQLiteStatement __preparedStmtOfClearDone;

  public OfflineQueueDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfOfflineQueueItem = new EntityInsertionAdapter<OfflineQueueItem>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `offline_queue` (`id`,`audioPath`,`contextNote`,`queuedAt`,`status`) VALUES (nullif(?, 0),?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final OfflineQueueItem entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getAudioPath() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getAudioPath());
        }
        if (entity.getContextNote() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getContextNote());
        }
        statement.bindLong(4, entity.getQueuedAt());
        if (entity.getStatus() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getStatus());
        }
      }
    };
    this.__deletionAdapterOfOfflineQueueItem = new EntityDeletionOrUpdateAdapter<OfflineQueueItem>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `offline_queue` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final OfflineQueueItem entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__preparedStmtOfMarkProcessing = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE offline_queue SET status = 'PROCESSING' WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfMarkDone = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE offline_queue SET status = 'DONE' WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfClearDone = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM offline_queue WHERE status = 'DONE'";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final OfflineQueueItem item, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfOfflineQueueItem.insertAndReturnId(item);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final OfflineQueueItem item, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfOfflineQueueItem.handle(item);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object markProcessing(final int id, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfMarkProcessing.acquire();
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
          __preparedStmtOfMarkProcessing.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object markDone(final int id, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfMarkDone.acquire();
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
          __preparedStmtOfMarkDone.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object clearDone(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfClearDone.acquire();
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
          __preparedStmtOfClearDone.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<OfflineQueueItem>> getPending() {
    final String _sql = "SELECT * FROM offline_queue WHERE status = 'PENDING' ORDER BY queuedAt ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"offline_queue"}, new Callable<List<OfflineQueueItem>>() {
      @Override
      @NonNull
      public List<OfflineQueueItem> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfAudioPath = CursorUtil.getColumnIndexOrThrow(_cursor, "audioPath");
          final int _cursorIndexOfContextNote = CursorUtil.getColumnIndexOrThrow(_cursor, "contextNote");
          final int _cursorIndexOfQueuedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "queuedAt");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final List<OfflineQueueItem> _result = new ArrayList<OfflineQueueItem>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final OfflineQueueItem _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpAudioPath;
            if (_cursor.isNull(_cursorIndexOfAudioPath)) {
              _tmpAudioPath = null;
            } else {
              _tmpAudioPath = _cursor.getString(_cursorIndexOfAudioPath);
            }
            final String _tmpContextNote;
            if (_cursor.isNull(_cursorIndexOfContextNote)) {
              _tmpContextNote = null;
            } else {
              _tmpContextNote = _cursor.getString(_cursorIndexOfContextNote);
            }
            final long _tmpQueuedAt;
            _tmpQueuedAt = _cursor.getLong(_cursorIndexOfQueuedAt);
            final String _tmpStatus;
            if (_cursor.isNull(_cursorIndexOfStatus)) {
              _tmpStatus = null;
            } else {
              _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            }
            _item = new OfflineQueueItem(_tmpId,_tmpAudioPath,_tmpContextNote,_tmpQueuedAt,_tmpStatus);
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
  public Flow<Integer> getPendingCount() {
    final String _sql = "SELECT COUNT(*) FROM offline_queue WHERE status = 'PENDING'";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"offline_queue"}, new Callable<Integer>() {
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
